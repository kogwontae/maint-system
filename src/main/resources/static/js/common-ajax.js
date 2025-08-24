var $successMsgBox = $('.task-success');
var $errorMsgBox = $('.task-danger');

const api = {
	API_GET_CORPORATION_LIST: {url: '/api/get-corporation/', success: successGetCorporations, fail: failureGetCorporations, dataType: 'json'},
	API_GET_LINE_LIST_BY_CORPORATION: {url: '/api/get-lines-by-corporation/', success: successGetLinesByCorporation, fail: failureGetLinesByCorporation, dataType: 'json'},
	API_GET_BUS_LIST_BY_LINE_CORPORATION: {url: '/api/get-buses-by-line-corporation/', success: successGetBusesByLineCorporation, fail: failureGetBusesByLineCorporation, dataType: 'json'},
  API_GET_BUS_LIST_BY_LINE_CORPORATION_REPORT: {url: '/api/get-buses-by-line-corporation-report/', success: successGetBusesByLineCorporation, fail: failureGetBusesByLineCorporation, dataType: 'json'},
	API_DOWNLOAD_DAILY_REPORTS_CSV: {url: '/order/daily-reports/csv/', success: successDownload, fail: failureCommonApi, param: {fileName: '日報CSV', ext: '.csv'}},
  API_GET_ORDER_SECTION_CONFIRM: {url: '/order/manager/change/section/confirm/api/', success: successOrderSectionConfirm, fail: failureCommonApi},
	API_DOWNLOAD_ROUTES_CSV: {url: '/service/bus-routes/csv/', success: successDownload, fail: failureCommonApi, param: {fileName: '系統CSV', ext: '.csv'}},
	API_DOWNLOAD_PERMISSION_CSV: {url: '/system/permissions/csv/', success: successDownload, fail: failureCommonApi, param: {fileName: '権限CSV', ext: '.csv'}},
  API_PERFORMANCE_CSV_DOWNLOAD: {url: '/service/performance/csv/download/', success: successDownload, fail: failureCommonApi, param: {ext: '.csv'}},
	API_DOWNLOAD_SERVICE_CORP_CSV: {url: '/master/service-corps/csv/', success: successDownload, fail: failureCommonApi, param: {fileName: '運行会社CSV', ext: '.csv'}},
	API_DOWNLOAD_VEHICLES_CSV: {url: '/master/vehicles/csv/', success: successDownload, fail: failureCommonApi, param: {fileName: '車両CSV', ext: '.csv'}},
	API_DOWNLOAD_BUS_STOP_CSV: {url: '/master/bus-stops/csv/', success: successDownload, fail: failureCommonApi, param: {fileName: '停留所CSV', ext: '.csv'}},
	API_DOWNLOAD_BUS_STOP_GROUP_CSV: {url: '/master/bus-stop-grps/csv/', success: successDownload, fail: failureCommonApi, param: {fileName: '停留所グループCSV', ext: '.csv'}},
	API_DOWNLOAD_AGENCY_CSV: {url: '/master/agencies/csv/', success: successDownload, fail: failureCommonApi, param: {fileName: '代理店CSV', ext: '.csv'}},
	API_DOWNLOAD_LINES_CSV: {url: '/service/bus-lines/csv/', success: successDownload, fail: failureCommonApi, param: {fileName: '路線CSV', ext: '.csv'}},
	API_DOWNLOAD_MEMBERS_CSV: {url: '/site/members/csv/', success: successDownload, fail: failureCommonApi, param: {fileName: '会員CSV', ext: '.csv'}},
	API_DOWNLOAD_CITIZEN_MEMBERS_CSV: {url: '/site/citizen-members/csv/', success: successDownload, fail: failureCommonApi, param: {fileName: '市民会員CSV', ext: '.csv'}},
	API_DOWNLOAD_USERS_CSV: {url: '/system/users/csv/', success: successDownload, fail: failureCommonApi, param: {fileName: 'ユーザCSV', ext: '.csv'}},
	API_DOWNLOAD_AGENCY_USERS_CSV: {url: '/system/agency-users/csv/', success: successDownload, fail: failureCommonApi, param: {fileName: "代理店ユーザCSV", ext: '.csv'}},
	API_DOWNLOAD_CREW_TERMINALS_CSV: {url: '/system/crew-terminals/csv/', success: successDownload, fail: failureCommonApi, param: {fileName: "乗務員端末CSV", ext: '.csv'}},
  API_CHANGE_ORDER_COMMENT: {url: '/api/service/order-manager/change/order/comment', success: successChangeMessage, fail: failChangeMessage},
  API_GET_LINES: {url: '/api/order/specialist/lines', type: 'GET', success: () => {return (data) => data}, fail: () => {return () => failureCommon('システムエラーが発生しました。')}},
  API_CHANGE_ORDER_DETAIL: { url: '/api/service/order-manager/change/order/detail/', success: successChangeMessage, fail: failOrderChange},
  API_GET_ORDER_CHANGE_HISTORIES: {url: '/api/service/order-manager/get-order-change-histories/', success: successGetOrderChangeHistory, fail: failGetOrderChangeHistory},
}

class CommonAjax {
	constructor(api, data) {
		this.type = api.type??"POST";
		this.url = api.url;
		this.data = data;
		this.dataType = api?.dataType;
		this.successFunction = api.success;
		this.failureFunction = api.fail;
		this.param = api.param;
	}

	callApi() {
		return $.ajax({
			type: this.type,
			url: this.url,
			data: this.data,
			dataType: this.dataType,
			async: false,
			timeout: 100000,
		}).then(this.successFunction(this.param),
						this.failureFunction()
		);
	}
}

/** 共通エラー */
function failureCommon(data) {
  const messages = data.responseJSON;
  if (messages instanceof Array) {
    const errors = [...messages].map(message => `<div class="task-text">${message}</div>`);
    $('.task-danger').append(errors).closest('#messageBox').show();
    return;
  }

  const message = data.responseText;

  /** undefinedの場合は文字列を取得したと判断してdataの中身を直接出力する */
  if(message == undefined){
    $('.task-danger').closest('#messageBox').show();
    const redMessageStyle = getRedMessageStyle(data);
    $('.task-danger').append(`<div class="task-text"${redMessageStyle}>${data}</div>`).show();
    return;
  }

  /** エラーメッセージ設定 */
  $('.task-danger').closest('#messageBox').show();
  const redMessageStyle = getRedMessageStyle(message);
  $('.task-danger').append(`<div class="task-text"${redMessageStyle}>${message}</div>`).show();
}

/** 対象のエラーメッセージが表示されるとき、文字色を赤く表示する */
function getRedMessageStyle(message) {
  const redMessages = ['料金が異なるため、変更できません。'];
  const redMessageStyle = redMessages.includes(message)
    ? ` style="color: red;"`
    : ``;
  return redMessageStyle;
}

/** 会社一覧取得(成功) */
function successGetCorporations() {
  return (data) => {
    let appendList = '';
    if(data.length === 0) {
      failureCommon(`会社が存在しません。`);
      return;
    }
    data.forEach(e => {
        appendList += `<option class="searchCorporationId" value="${e.serviceCorporationId}">${e.serviceCorporationName}</option>`;
    });
    $('#searchCorporationId').append(appendList);
  }
}

/** 会社一覧取得(失敗) */
function failureGetCorporations() {
  return () => failureCommon('会社の取得に失敗しました。');
}

/** 路線一覧取得(成功) */
function successGetLinesByCorporation() {
  return (data) => {
    let appendList = '';
    if(data.length === 0) {
      failureCommon(`路線が存在しません。`);
      return;
    }
    data.forEach(e => {
      appendList += `<option class="searchLineId" value="${e.lineId}">${e.lineCode}:${e.lineName}</option>`;
    });
    $('#searchLineId').append(appendList);
  }
}

/** 路線一覧取得(失敗) */
function failureGetLinesByCorporation() {
  return () => failureCommon('路線の取得に失敗しました。');
}

/** 便号車一覧取得(成功) */
function successGetBusesByLineCorporation() {
  return (data) => {
    let appendList = '';
    if(data.length === 0) {
      failureCommon(`号車が存在しません。`);
      return;
    }
    data.forEach(e => {
      appendList += `<option class="searchBusId" value="${e.busId}">${e.busServiceName} 便（${e.busName} 号車）</option>`;
    });
    $('#searchBusId').append(appendList);
  }
}

/** 便号車一覧取得(失敗) */
function failureGetBusesByLineCorporation() {
  return () => failureCommon('便号車の取得に失敗しました。');
}

/** ダウンロード(成功) */
function successDownload(param) {
	return (data) => {
		// 日本語文字化け回避
		let bom = new Uint8Array([0xEF, 0xBB, 0xBF]);
		let blob = new Blob([bom, data], {
			type: 'text/csv'
		});
		let url = (window.URL || window.webkitURL).createObjectURL(blob);
		let link = document.createElement('a');
		// ファイル名
		link.download = param.fileName + '_' + $.now() + param.ext;
		link.href = url;
		document.body.appendChild(link);
		link.click();
		document.body.removeChild(link);
	}
}

/** API失敗 共通処理 */
function failureCommonApi() {
  return (data) => failureCommon(data);
}

/** API失敗 共通処理 */
function failureConfirmCommonApi() {
  $('#confirmBtn').prop('disabled', true);
  return (data) => failureCommon(data.responseText);
}

/** Ajax通信START */
function connectAjax(url, jsonData, $targetObj) {

  $.ajax({
    type : "POST",
    url : url,
    data : jsonData,
    dataType : 'json',
    timeout : 100000,
    success : function(data) {
      setAjaxResultData(url, data, $targetObj);
    },
    error : function(e) {
      setAjaxResultData(url, null, $targetObj);
    }
  });
}

/** Ajax通信START */
function connectAjaxWithRequstBody(url, request, $targetObj) {

  $.ajax({
    type: 'POST',
    url: url,
    data: JSON.stringify(request),
    contentType: 'application/json',
    timeout : 100000,
    success : function(data, textStatus, jqXHR) {
      setAjaxResultData(url, data, $targetObj, jqXHR.status);
    },
    error : function(res) {
      setAjaxResultData(url, res, $targetObj);
    }
  });
}

/** Ajax通信結果メッセージ表示 */
function showAjaxResultMessage(result, message) {
    $successMsgBox.empty();
    $errorMsgBox.empty();
    if(result){
        $successMsgBox.closest('#messageBox').show();
        $errorMsgBox.closest('#messageBox').hide();
        $successMsgBox.append('<div class="task-text">' + message + '</dev>');
    } else {
        $successMsgBox.closest('#messageBox').hide();
        $errorMsgBox.closest('#messageBox').show();
        $errorMsgBox.append('<div class="task-text">' + message + '</dev>');
    }
    return;
}
/** 変更成功メッセージ表示 */
function successChangeMessage(){
  return (data) => showAjaxResultMessage(data, '注文情報変更を完了しました。');
}
/** 変更失敗メッセージ表示 */
function failChangeMessage(){
  return () => failureCommon('注文情報変更に失敗しました。');
}

/** 注文情報変更失敗メッセージ表示 */
function failOrderChangeMessage(data) {
  if (data.status === 400 && data.responseText === '入力内容が正しくありません。') {
    // 入力内容チェック
    checkInputData();
    failureCommon('入力内容が正しくありません。');
  } else {
    failureCommon('注文情報変更に失敗しました。');
  }
}

/** 注文情報変更失敗 */
function failOrderChange(data){
  return (data) => failOrderChangeMessage(data);
}

/** Ajax通信エラー */
function errorAjaxConnect(message) {
  $errorMsgBox.empty();
  $errorMsgBox.closest('.panel-message').show();
  $errorMsgBox.append('<div class="task-text">' + message + '</dev>');
  return;
}
/** MODAL_Ajax通信結果メッセージ表示 */
function showAjaxResultMessageInModal(result, message) {
  $msgBox = result ? $('#modalSuccessMsgBox') : $('#modalErrorMsgBox');
  $msgBox.empty();
  $msgBox.show();
  $msgBox.append('<li>' + message + '</li>');
  return;
}

/** 区間変更確認処理 */
function successOrderSectionConfirm() {
  return (data) => {
    // 料金情報
    const {chargeInfos, beforeTotalCharge, afterTotalCharge, paymentInfos, beforeTotalPayment, afterTotalPayment} = JSON.parse(data);

    // trタグ作成
    const chargeTrs = chargeInfos.map(createChargeTr);
    const paymentTrs = paymentInfos.map(createPaymentTr);
    $('#paymentTbody').empty().append(paymentTrs)
    const totalChargeTr = createTotalChargeTr(beforeTotalCharge, afterTotalCharge);
    const noCountTotalRefundPayment = [...document.querySelectorAll('.noCountRefund')].map(e => Number(e.dataset.refund)).reduce((a, b) => a + b, 0);
    const countTotalRefundPayment = [...document.querySelectorAll('.countRefund')].map(e => Number(e.dataset.refund)).reduce((a, b) => a + b, 0);
    const totalRefundPayment = beforeTotalPayment - afterTotalPayment;
    const refundText = totalRefundPayment > 0 ? '返戻額' : '追加料金';
    const countRefundText = totalRefundPayment > 0 ? '窓口払戻額' : '窓口受取額';
    $('#addChargeTh').text(refundText);
    $('#addCountChargeTh').text(countRefundText);
    const totalPaymentTr = createTotalPaymentTr(beforeTotalPayment, afterTotalPayment, Math.abs(noCountTotalRefundPayment), Math.abs(countTotalRefundPayment));
    $('#chargeTbody').empty().append(chargeTrs).append(totalChargeTr);
    $('#paymentTbody').append(totalPaymentTr);
    $('#confirmBtn').prop('disabled', false);
    page_content_onresize();
  }
}

/** 合計料金tr */
function createTotalChargeTr(beforeTotalCharge, afterTotalCharge) {
  return `<tr role="row">
            <td colspan="6" style="text-align: right;">合計</td>
            <td style="text-align: right;">${numberToCommaYen(beforeTotalCharge)}</td>
            <td style="text-align: right;">${numberToCommaYen(afterTotalCharge)}</td>
          </tr>`;
}

/** 合計決済tr */
function createTotalPaymentTr(beforeTotalPayment, afterTotalPayment, totalNoCountRefundPayment, totalCountRefundPayment) {
  return `<tr role="row">
            <td colspan="2" style="text-align: right;">合計</td>
            <td style="text-align: right;">${numberToCommaYen(beforeTotalPayment)}</td>
            <td style="text-align: right;">${numberToCommaYen(afterTotalPayment)}</td>
            <td style="text-align: right;">${numberToCommaYen(totalNoCountRefundPayment)}</td>
            <td style="text-align: right; color: red">${numberToCommaYen(totalCountRefundPayment)}</td>
            <td></td>
          </tr>`;
}

/** 料金tr */
const createChargeTr = (e, index) => {
  /** 株主優待か? */
  const isBenefitTicket = (chargeDivision, discountDivision) => chargeDivision === '02' && discountDivision === '04';
  return `<tr role="row">
            <td>${index + 1}</td>
            <td>${e.chargeDivisionText}</td>
            <td>${e.chargeDetailText}${isBenefitTicket(e.chargeDivision, e.discountDivision) ? selectBenefitTicketReceiveDivision(e.benefitTicketReceiveDivision) : ''}</td>
            <td>${e.busReservationNo}</td>
            <td>${e.passengerText}</td>
            <td>${e.seatSelectionFlag ? e.seatInfoText : '[' + e.seatInfoText + ']'}</td>
            <td style="text-align: right;">${numberToCommaYen(e.beforeCharge)}</td>
            <td style="text-align: right;">${numberToCommaYen(e.afterCharge)}</td>
          </tr>`;
}

// 株主優待受取区分
const benefitTicketReceiveDivisionObj = new Map();
benefitTicketReceiveDivisionObj.set("01", "未受取");
benefitTicketReceiveDivisionObj.set("02", "受取済(窓口)");
benefitTicketReceiveDivisionObj.set("03", "受取済(乗車時)");

/** 株主優待受取区分select */
const selectBenefitTicketReceiveDivision = benefitTicketReceiveDivision =>
  ` <select class="form-control benefitTicketSelect">
      ${optionBenefitTicketReceiveDivision(benefitTicketReceiveDivision)}
    </select>`;

/** 株主優待受取区分option */
const optionBenefitTicketReceiveDivision = benefitTicketReceiveDivision => {
  let options = '';
  for ([key, value] of benefitTicketReceiveDivisionObj) {
    const selected = key === benefitTicketReceiveDivision ? ' selected' : '';
    options += `<option value="${key}"${selected}>${value}</option>`
  }
  return options;
}

/** 数字形式変換 */
function numberToCommaYen(val) {
  if(val === undefined) return '-';
  return val.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',') + '円';
}

// 受取区分
const receiveDivisionObj = new Map();
receiveDivisionObj.set("00", "未入金（乗車時受取）");
receiveDivisionObj.set("01", "現金");
receiveDivisionObj.set("02", "クレジット決済");
receiveDivisionObj.set("03", "株主優待");

/** 決済tr */
const createPaymentTr = (e, index) => {
  const receiveDivisionText = e.receiveDivision ? `<br>${receiveDivisionObj.get(e.receiveDivision)}` : '';
  const beforePaymentAmount = e.beforePaymentAmount ?? 0;
  let noCountAfterAmount = 0;
  let noCountAfterAmountText = '';
  let countAfterAmount = 0;
  let countAfterAmounText = '';
  if(e.paymentDivision === '04'){
    noCountAfterAmountText = '-';
    countAfterAmounText = numberToCommaYen(Math.abs(beforePaymentAmount - e.afterPaymentAmount));
    countAfterAmount = Math.abs(beforePaymentAmount - e.afterPaymentAmount);
  }else{
    noCountAfterAmountText = numberToCommaYen(Math.abs(beforePaymentAmount - e.afterPaymentAmount));
    noCountAfterAmount = Math.abs(beforePaymentAmount - e.afterPaymentAmount);
    countAfterAmounText = '-';
  }
  return `<tr role="row">
            <td>${index + 1}</td>
            <td style="text-align: right;">${e.paymentDivisionText}${receiveDivisionText}</td>
            <td style="text-align: right;">${numberToCommaYen(e.beforePaymentAmount)}</td>
            <td style="text-align: right;">${numberToCommaYen(e.afterPaymentAmount)}</td>
            <td style="text-align: right;" class="noCountRefund" data-refund="${noCountAfterAmount}">${noCountAfterAmountText}</td>
            <td style="text-align: right; color: red" class="countRefund" data-refund="${countAfterAmount}">${countAfterAmounText}</td>
            <td>${e.isNeedCashDivision ? selectReceiveDivision() : ''}</td>
          </tr>`;
}

/** 受取区分option */
const optionReceiveDivision = () => {
  let options = '';
  let i = 0;
  for ([key, value] of receiveDivisionObj) {
    if (key === '00') continue;
    const selected = i === 0 ? ' selected' : '';
    options += `<option value="${key}"${selected}>${value}</option>`
    i++;
  }
  return options;
}

/** 受取区分select */
const selectReceiveDivision = () =>
  `<select class="receiveDivision" name="receiveDivision">
    ${optionReceiveDivision()}
  </select>`;

/** 注文変更履歴取得の成功時処理 */
function successGetOrderChangeHistory() {
  return (data) => {
    // エラーメッセージ初期化
    $errorMsgBox.empty();
    $errorMsgBox.closest('#messageBox').hide();
    // 操作履歴MODAL
    var $modal = $('#order-history-info-modal');
    var $contentArea = $modal.find('tbody');
    // 履歴を作成
    $contentArea.empty();
    $(data).each(function(idx, obj) {
      var html = '';
      html += `<tr role="row">
                 <td>${obj.procDateWithSlash}</td>
                 <td>${obj.procDivisionText}</td>
                 <td>${obj.procDestDivision4ChangeHistoryDisp}</td>
                 <td>${obj.procUserId ? obj.procUserId : 'ー'}</td>
               </tr>`;
      $contentArea.append(html);
    });
    $modal.modal('show');
    page_content_onresize();
  }
}

/** 注文変更履歴取得の失敗時処理 */
function failGetOrderChangeHistory() {
  return (data) => {
    // エラーメッセージ初期化
    $errorMsgBox.empty();
    $errorMsgBox.closest('#messageBox').hide();
    // 操作履歴MODALにエラーメッセージ設定
    var $modal = $('#order-history-info-modal');
    var message = `<div class="task-text modal-message">${data.responseText}</div>`;
    $modal.find('.task-item.task-danger').append(message).show().closest('#custom-messageBox').show();
    $modal.modal('show');
    page_content_onresize();
  }
}
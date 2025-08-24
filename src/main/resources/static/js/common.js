/** 二重クリック禁止 */
$('.double-click').on('click', function() {
	$(this).prop('disabled', true);
	const formAction = $(this).attr('formaction');
	if (formAction !== undefined) {
		$(this).closest('form').attr('action', formAction).submit();
		return;
	}
	$(this).closest('form').submit();
});

/**
 * set header CSRF token for AJAX call
 *
 * @returns
 */
$(function() {
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	$(document).ajaxSend(function(e, xhr, options) {
		xhr.setRequestHeader(header, token);
	});

	// Select2の設定
	if ($('.select2').length) {
		$(".select2").select2({
		    language: "ja",
		    dropdownAutoWidth: true,
		    width: '100%',
			templateResult: resultState
		});
		// 必須エラー等がある場合、背景色を変更する
		$('.select2.error').parent().find(".select2-selection").css("background-color", "pink");
	}

	function resultState(data, container) {
		if (data.element) {
			$(container).addClass($(data.element).attr("class"));
		}
		return data.text;
	}

});

/** type="number"のmaxlength制限 */
$(document).on('input', 'input[type="number"]', function(){
	if($(this).attr('maxlength') !== undefined && this.value.length > this.maxLength) {
		this.value = this.value.slice(0, this.maxLength);
	}
});

/** 注文詳細画面に戻る */
$('#back-order-detail-button').on('click', function() {
  $('#order-detail-form').submit();
});

/** Select2にフォーカスが当たっているときに、標準ではEnterで開くが、↓で開くようにする  */
$(document).on('keydown', '.select2', function(e) {
  if (e.originalEvent && e.which == 40) {
    e.preventDefault();
    $(this).siblings('select').select2('open');
  }
});

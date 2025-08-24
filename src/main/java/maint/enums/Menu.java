package maint.enums;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import maint.dto.UserPermissionDto;
import maint.session.UserSession;
import maint.utils.ExObjectUtils;
import org.apache.commons.lang3.StringUtils;
import lombok.Getter;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * メニュー
 *
 * @author kang
 */
@Getter
public enum Menu {

  /** システム管理 */
  SYSTEM("システム管理", "01", "SYSTEM000", null, "#", null, "fa fa-cog"),
  /** 権限管理 */
  PERMISSION("権限管理", "02", "SYSTEM002", "SYSTEM000", "/system/permissions/?init=1", SYSTEM, "UI002"),
  /** アカウント管理 */
  ACCOUNT("アカウント管理", "02", "SYSTEM003", "SYSTEM000", "/system/users/?init=1", SYSTEM, "UI003"),

  /** 基本情報 */
  BASE_INFO("基本情報", "01", "BASEINFO000", null, null, null, "null"),
  /** 事業者管理 */
  OPERATOR("事業者管理", "02", "BASEINFO001", "BASEINFO000", null, BASE_INFO, "UI004"),
  /** 運行会社管理 */
  OP_COMPANY("運行会社管理", "02", "BASEINFO002", "BASEINFO000", null, BASE_INFO, "UI005"),
  /** 営業所管理 */
  OFFICE("営業所管理", "02", "BASEINFO003", "BASEINFO000", null, BASE_INFO, "UI006"),
  /** 車両管理 */
  VEHICLE("車両管理", "02", "BASEINFO004", "BASEINFO000", null, BASE_INFO, "UI007"),
  /** 停留所管理 */
  STOP("停留所管理", "02", "BASEINFO005", "BASEINFO000", null, BASE_INFO, "UI008"),
  /** 標柱管理 */
  MARKER("標柱管理", "02", "BASEINFO006", "BASEINFO000", null, BASE_INFO, "UI009"),

  /** 運行管理 */
  OPERATION("運行管理", "01", "OPERATION000", null, null, null, "null"),
  /** 運行カレンダー管理 */
  OP_CALENDAR("運行カレンダー管理", "02", "OPERATION001", "OPERATION000", null, OPERATION, "UI010"),
  /** 祝日カレンダー管理 */
  HOLIDAY_CALENDAR("祝日カレンダー管理", "02", "OPERATION002", "OPERATION000", null, OPERATION, "UI011"),
  /** 路線管理 */
  ROUTE("路線管理", "02", "OPERATION003", "OPERATION000", null, OPERATION, "UI012"),
  /** 系統管理 */
  SYSTEM_LINE("系統管理", "02", "OPERATION004", "OPERATION000", null, OPERATION, "UI013"),
  /** 便ダイヤ改正管理 */
  TIMETABLE_REVISION("便ダイヤ改正管理", "02", "OPERATION005", "OPERATION000", null, OPERATION, "UI014"),
  /** 便ダイヤ管理 */
  TIMETABLE("便ダイヤ管理", "02", "OPERATION006", "OPERATION000", null, OPERATION, "UI015"),
  /** 日毎便ダイヤ管理 */
  DAILY_TIMETABLE("日毎便ダイヤ管理", "02", "OPERATION007", "OPERATION000", null, OPERATION, "UI016"),
  /** 運賃改定管理 */
  FARE_REVISION("運賃改定管理", "02", "OPERATION008", "OPERATION000", null, OPERATION, "UI017"),
  /** 運賃管理 */
  FARE("運賃管理", "02", "OPERATION009", "OPERATION000", null, OPERATION, "UI018"),
  /** 棒ダイヤ表示 */
  DIAGRAM_VIEW("棒ダイヤ表示", "02", "OPERATION010", "OPERATION000", null, OPERATION, "UI019"),
  /** 車両動態管理 */
  VEHICLE_MOVEMENT("車両動態管理", "02", "OPERATION011", "OPERATION000", null, OPERATION, "UI020"),
  /** 運行実績管理 */
  OPERATION_RECORD("運行実績管理", "02", "OPERATION012", "OPERATION000", null, OPERATION, "UI021"),
  /** 停留所情報インポート */
  IMPORT_STOP("停留所情報インポート", "02", "OPERATION013", "OPERATION000", null, OPERATION, "UI025"),
  /** 便情報インポート */
  IMPORT_TRIP("便情報インポート", "02", "OPERATION014", "OPERATION000", null, OPERATION, "UI026"),
  /** 運賃情報インポート */
  IMPORT_FARE("運賃情報インポート", "02", "OPERATION015", "OPERATION000", null, OPERATION, "UI027"),

  /** 販売管理 */
  SALES("販売管理", "01", "SALES000", null, null, null, "null"),
  /** 収入情報インポート */
  IMPORT_REVENUE("収入情報インポート", "02", "SALES001", "SALES000", null, SALES, "UI024"),

  /** 帳票出力 */
  REPORT("帳票出力", "01", "REPORT000", null, null, null, "null"),
  /** GTFS-Schedule */
  GTFS("GTFS-Schedule", "02", "REPORT001", "REPORT000", null, REPORT, "UI022"),
  /** キロ程表出力 */
  DISTANCE_REPORT("キロ程表出力", "02", "REPORT002", "REPORT000", null, REPORT, "UI029"),
  /** 停留所付近図出力 */
  STOP_MAP("停留所付近図出力", "02", "REPORT003", "REPORT000", null, REPORT, "UI030"),
  /** 路線時刻表出力 */
  ROUTE_TIMETABLE("路線時刻表出力", "02", "REPORT004", "REPORT000", null, REPORT, "UI031"),
  /** スターフ出力 */
  STAFF_REPORT("スターフ出力", "02", "REPORT005", "REPORT000", null, REPORT, "UI032"),
  /** 停留所一覧出力 */
  STOP_LIST("停留所一覧出力", "02", "REPORT006", "REPORT000", null, REPORT, "UI033"),
  /** 標柱一覧出力 */
  MARKER_LIST("標柱一覧出力", "02", "REPORT007", "REPORT000", null, REPORT, "UI034"),
  /** 標柱停留所一覧出力 */
  MARKER_STOP_LIST("標柱停留所一覧出力", "02", "REPORT008", "REPORT000", null, REPORT, "UI035"),
  /** 系統一覧出力 */
  LINE_LIST("系統一覧出力", "02", "REPORT009", "REPORT000", null, REPORT, "UI036"),
  /** 便ダイヤ一覧出力 */
  TRIP_LIST("便ダイヤ一覧出力", "02", "REPORT010", "REPORT000", null, REPORT, "UI037"),
  /** 運行回数出力 */
  RUN_COUNT("運行回数出力", "02", "REPORT011", "REPORT000", null, REPORT, "UI038"),
  /** 運賃表出力 */
  FARE_TABLE("運賃表出力", "02", "REPORT012", "REPORT000", null, REPORT, "UI039")

  /** */
  ;

  /** 名称 */
  private final String name;
  /** メニュー区分 */
  private final String menuDivision;
  /** メニューコード */
  private final String menuCode;
  /** 親メニューコード */
  private final String parentCode;
  /** URI */
  private final String uri;
  /** 親メニュー */
  private final Menu parentsMenu;
  /** Function (画面ID) */
  private final String function;

  Menu(String name, String menuDivision, String menuCode, String parentCode, String uri, Menu parentsMenu, String function) {
    this.name = name;
    this.menuDivision = menuDivision;
    this.menuCode = menuCode;
    this.parentCode = parentCode;
    this.uri = uri;
    this.parentsMenu = parentsMenu;
    this.function = function;
  }

  /**
   * 親メニューが存在するかを判定する。
   *
   * @return true:親メニューあり, false:親メニューなし
   */
  public boolean hasParentMenu() {
    return this.parentsMenu != null;
  }

  /**
   * 該当メニューコードのメニュー取得
   *
   * @param menuCode メニューコード
   * @return menuValues
   */
  public static Menu getByMenuCode(String menuCode) {
    if (StringUtils.isBlank(menuCode))
      return null;
    for (Menu menuValues : Menu.values()) {
      if (menuValues.getMenuCode().equals(menuCode)) {
        return menuValues;
      }
    }
    return null;
  }

  /**
   * 第1階層のメニューを返す
   *
   * @return 第1階層のメニュー
   */
  public static Menu[] getTopMenuValues(List<UserPermissionDto> userPermList) {
    List<Menu> topMenuValuesList = new ArrayList<>();
    for (Menu menuValues : Menu.values()) {
      if (menuValues.parentsMenu == null) {
        topMenuValuesList.add(menuValues);
      }
    }
    // menuCodeを基準にMap変換
    Map<String, Integer> permMap = userPermList.stream().collect(
        Collectors.toMap(UserPermissionDto::getParentMenuCode,
            UserPermissionDto::getParentDisplayOrder, (v1, v2) -> v1
        ));

    // topMenuListフィルター（表示順ソート）
    List<Menu> filteredAndSortedMenuList =
        topMenuValuesList.stream().filter(menu -> permMap.containsKey(menu.getMenuCode()))
            .sorted(Comparator.comparingInt(menu -> permMap.get(menu.getMenuCode()))).toList();

    return filteredAndSortedMenuList.toArray(new Menu[filteredAndSortedMenuList.size()]);
  }

  /**
   * 親メニューの子メニューを返す
   *
   * @param targetMenu 親メニュー
   * @return 子メニュー
   */
  public static Menu[] getSubMenu(Menu targetMenu, List<UserPermissionDto> userPermList) {

    List<Menu> subMenuList = new ArrayList<>();

    for (Menu menuValue : Menu.values()) {
      if (menuValue.getParentsMenu() == targetMenu) {
        if (!subMenuList.contains(menuValue)) {
          subMenuList.add(menuValue);
        }
      }
    }

    // menuCodeを基準にMap変換
    Map<String, Integer> permMap = userPermList.stream().collect(
        Collectors.toMap(UserPermissionDto::getMenuCode, UserPermissionDto::getDisplayOrder,
            (v1, v2) -> v1
        ));

    // subMenuListフィルター（表示順ソート）
    List<Menu> filteredAndSortedMenuList =
        subMenuList.stream().filter(menu -> permMap.containsKey(menu.getMenuCode()))
            .sorted(Comparator.comparingInt(menu -> permMap.get(menu.getMenuCode()))).toList();

    return filteredAndSortedMenuList.toArray(new Menu[filteredAndSortedMenuList.size()]);
  }

  /**
   * 全ての子メニューを返す
   *
   * @return 全ての子メニュー
   */
  public static Menu[] getAllSubMenu() {
    List<Menu> subMenuList = new ArrayList<>();

    for (Menu menuValue : Menu.values()) {
      if (menuValue.hasParentMenu()) {
        subMenuList.add(menuValue);
      }
    }
    return subMenuList.toArray(new Menu[subMenuList.size()]);
  }

  /**
   * SideMenu Active設定(TOPMENU)
   *
   * @param topMenu Menu
   * @param url     url
   * @return true: 選択TOPMENU, false: 未選択TOPMENU
   */
  public static boolean isActiveTopMenu(Menu topMenu, String url) {
    UserSession session = (UserSession) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    for (Menu sub : getSubMenu(topMenu, session.getUserPermList())) {
      if (isActiveSubMenu(sub, url)) {
        return true;
      }
    }
    return false;
  }

  /**
   * SideMenu Active設定(SUBMENU)
   *
   * @param subMenu Menu
   * @param url     url
   * @return true: 選択SUBMENU, false: 未選択SUBMENU
   */
  public static boolean isActiveSubMenu(Menu subMenu, String url) {
    return ExObjectUtils.isNotBlank(subMenu.uri) && url.startsWith(subMenu.uri.split("\\?")[0]);
  }

}

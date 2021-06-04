package com.yh.trading.constant;

/**
 * 路由名管理类
 *
 * @author guowanxin
 */
public interface ARouterPathManager {

    int INDEX_HOME = 0;
    int INDEX_CATEGORY = 1;
    int INDEX_FAVORITE = 2;
    int INDEX_CART = 3;
    int INDEX_MINE = 4;

    int RESULT_ADD = 1001;
    int RESULT_UPDATE = 1002;
    int RESULT_DEL = 1003;


    /**
     * 以下为跳转界面需要key
     */
    String KEY_GOOD_LIST = "goodList";
    String KEY_TITLE_NAME = "titleName";
    String KEY_PICKUP_PERSON = "pickUpPerson";
    String KEY_POSITION = "position";
    String KEY_PHONE = "phone";
    String KEY_SELECT_ID = "selectId";
    String KEY_ADDRESS_JSON = "addressJson";
    String KEY_INDEX = "index";
    String KEY_ITEM_ID = "itemId";//商品id
    String KEY_SHOP_ID = "shopId";//门店id
    String KEY_CITY_ID = "cityId";//城市id
    String KEY_URL = "url";
    String KEY_NAME = "name";
    String KEY_NAME_1 = "name_1";
    String KEY_NAME_2 = "name_2";
    String KEY_NAME_3 = "name_3";
    String KEY_ID = "id";
    String KEY_ID_1 = "id_1";
    String KEY_ID_2 = "id_2";
    String KEY_LAT = "latitude";
    String KEY_LON = "longitude";
    String KEY_ADDRESS = "address";
    String KEY_ADDRESS_1 = "address_1";
    String KEY_ADDRESS_2 = "address_2";
    String KEY_DELIVERY_TYPE = "deliveryType";
    String KEY_IDS = "ids";
    String KEY_JSON = "json";
    String KEY_NAVE_PATH = "nave_path";
    String KEY_BEAN = "bean";
    String KEY_ORDER_NO = "orderNo";
    String KEY_IS_SPLIT = "isSplit";
    String KEY_ORDER_AMOUNT = "orderAmount";
    String KEY_PAY_TYPE = "payType";
    String KEY_TYPE = "type";
    String KEY_INVOICE_AMOUNT = "invoiceAmount";
    String KEY_INVOICE_NO = "invoiceNo";
    String KEY_INVOICE_NUM = "invoiceNum";
    String KEY_STATUS = "status";


    /**
     * 以下为home模块路由名
     */
    String ActivityMain = "/app/ActivityMain";
    String ActivityGoodInfo = "/home/ActivityGoodInfo";
    String ActivityWeb = "/home/ActivityWeb";
    String FragmentShare = "/home/ShareFragment";
    String ServiceCallPhone = "/home/ServiceCallPhone";
    String ActivityShareGood = "/home/ActivityShareGood";
    String ActivityShareShop = "/home/ActivityShareShop";

    /**
     * 以下为login模块路由名
     */
    String ActivityLogin = "/login/ActivityLogin";
    String ActivityLoginInputUserInfo = "/login/ActivityLoginInputUserInfo";
    String ActivityLoginChooseAddress = "/login/ActivityLoginChooseAddress";
    String ActivityLoginChooseCity = "/login/ActivityLoginChooseCity";
    String ActivityLoginAgreement = "/login/ActivityLoginAgreement";

    /**
     * 以下为cart模块路由名
     */
    String ActivitySettleOrder = "/cart/ActivitySettleOrder";
    String ActivityOrderGood = "/cart/ActivityOrderGood";
    String ActivityUpdatePickUpPerson = "/cart/ActivityUpdatePickUpPerson";
    String ActivityCashier = "/cart/ActivityCashier";
    String ActivityPaySuccess = "/cart/ActivityPaySuccess";
    String ActivityCart = "/cart/ActivityCart";
    String ServiceCartData = "/cart/ServiceCartData";
    String ServiceAddCart = "/cart/ServiceAddCart";

    /**
     * 以下为category模块路由名
     */
    String FragmentCartEditDialog = "/category/FragmentCartEditDialog";
    String FragmentSelectAddressList = "/category/CategoryChooseBuyDialogFragment";

    /**
     * 以下为mine模块路由名
     */
    String ActivitySelectAddressList = "/mine/ActivitySelectAddressList";
    String ActivityMineAddressEdit = "/mine/ActivityMineAddressEdit";
    String ActivityMineInputInvoice = "/mine/ActivityMineInputInvoice";
    String ActivityMineInvoiceHistory = "/mine/ActivityMineInvoiceHistory";
    String ActivityMineInvoiceOrder = "/mine/ActivityMineInvoiceOrder";
    String ActivityMineInvoiceDetail = "/mine/ActivityMineInvoiceDetail";
    String ActivityMineInvoiceService = "/mine/ActivityMineInvoiceService";
    String ActivityMineLimit = "/mine/ActivityMineLimit";
    String ActivityMineFavorite = "/mine/ActivityMineFavorite";
    String ActivityMineSetting = "/mine/ActivityMineSetting";
    String ActivityMineLicenceInfo = "/mine/ActivityMineLicenceInfo";
    String ActivityMineVersionActivity = "/mine/ActivityMineVersion";
    String ActivityMineAddress = "/mine/ActivityMineAddress";
    String IProviderMineDataServiceImpl = "/mine/MineDataServiceImpl";


    /**
     * 以下为shop模块路由名
     */
    String ActivityShopDetail = "/shop/ActivityShopDetail";

    /**
     * 以下为order模块路由名
     */
    String ActivityOrderList = "/order/ActivityOrderList";
    String ActivityOrderDetail = "/order/ActivityOrderDetail";


    /**
     * 以下为search模块路由名
     */
    String ActivitySearchGoods = "/search/ActivitySearchGoods";
    String IProviderSearchGoodsListUIServiceImpl = "/search/SearchGoodsListUIServiceImpl";

    //intent requestCode
    int INTENT_REQUEST_CODE_1 = 1;
    int INTENT_REQUEST_CODE_2 = 2;
    int INTENT_REQUEST_CODE_3 = 3;

}

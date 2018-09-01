/**
     * login
     *
     * @param account    LoginName
     * @param password   password
     * @param loginType  loginType
     * @param tokenType  tokenType
     * @param sourceType sourceType
     * @return UserInfo
     */
    @FormUrlEncoded
    @POST(API.LOGIN)
    Observable<BaseResponse<UserInfo>> login(@Field("LoginName") String account, @Field("password") String password, @Field("loginType") String loginType, @Field("tokentype") String tokenType, @Field("SourceType") String sourceType);

    /**
     * 获取验证码
     */
    @POST(API.GET_CODE)
    Observable<BaseResponse<String>> getCode(@Body JsonObject requestBody);

    /**
     * 绑定
     */
    @POST(API.BINDALIPAY)
    Observable<BaseResponse<String>> getBangDing(@Body JsonObject requestBody);

    /**
     * 手机号码快速登录注册接口
     */
    @POST(API.FASTREGISTER)
    Observable<BaseResponse<UserInfo>> setPassword(@Body JsonObject requestBody);

    /**
     * 找回密码
     */
    @POST(API.RESETPASSWORD)
    Observable<BaseResponse<UserInfo>> resetPassword(@Body JsonObject requestBody);


    /**
     * 验证验证码
     */
    @POST(API.VERIFICATIONCODE)
    Observable<BaseResponse<String>> confirmVerifyCode(@Body JsonObject requestBody);


    /**
     * 快捷登录
     */
    @POST(API.QUICK_LOGIN)
    Observable<BaseResponse<UserInfo>> getQuicklogin(@Body JsonObject requestBody);

    /**
     * 申请开店 提交资料
     */
    @POST(API.SUBMIT)
    Observable<BaseResponse<ApplyShopBean>> getPutInfo(@Body JsonObject requestBody);

    /**
     * 申请开店 获取开店信息 用来判断时候审核通过的
     */
    @GET(API.GET_SHOPINFO)
    Observable<BaseResponse<shopInfoBeans>> getShopInfo();

    /**
     * 获取互投广告优惠券列表
     */
    @POST(API.ADVERTISING)
    Observable<BaseResponse<List<InterAdvertBean>>> getListOfCoupons(@Body JsonObject requestBody);

    /**
     * 阿里云
     *
     * @return
     */
    @GET(API.GET_AIL_TOKEN)
    Observable<BaseResponse<AliyunTokenBean>> getAliCloudInfo();


    /**
     * get user info
     */
    @GET(API.USER_INFO)
    Observable<BaseResponse<UserInfo>> getUserInfo();

    /**
     * 获取订单列表
     */
    @FormUrlEncoded
    @POST(API.ORDER_LIST)
    Observable<BaseResponse<List<OrderList>>> getOrderList(@Field("pageindex") int pageIndex, @Field("pagesize") int pageSize, @Field("ListOrderStatus") int orderStatus, @Field("lasttime") String lastTime);

    /**
     * 执行发货操作
     */
    @POST(API.LOGISTICS_SHIP)
    Observable<BaseResponse<Object>> doShip(@Body JsonObject requestBody);

    /**
     * 执行同意退款/同意退货
     */
    @FormUrlEncoded
    @POST(API.AGREED_CANCEL_ORDER)
    Observable<BaseResponse<Object>> agreedCancelOrder(@Field("ordercode") String orderCode);

    /**
     * 拒绝退款
     */
    @FormUrlEncoded
    @POST(API.REFUSE_TO_REFUND)
    Observable<BaseResponse<Object>> refuseToRefund(@Field("ordercode") String orderCode);

    /**
     * 普通订单详情
     *
     * @param orderCode 订单号
     * @param isShop    1:店铺
     */
    @FormUrlEncoded
    @POST(API.ORDER_DETAIL)
    Observable<BaseResponse<OrderDetail>> getOrderDetail(@Field("orderCode") String orderCode, @Field("isshop") int isShop);


    /**
     * 验证手机号码
     */
    @POST(API.GETACCOUNTISTHEREARE)
    Observable<BaseResponse<RegisterInfoBean>> getCheckPhone(@Body JsonObject requestBody);

    /**
     * 发布红包
     */
    @POST(API.GENERATE_RED_PACKET)
    Observable<BaseResponse<String>> getReleaseenvelopes(@Body JsonObject requestBody);


    /**
     * 获取商品管理列表
     *
     * @param pageIndex pageIndex
     * @param pageSize  pageSize
     * @param isShelves 是否上架
     * @param shopId    店铺id
     * @return
     */
    @GET(API.PRODUCT_MANAGER_LIST)
    Observable<BaseResponse<List<ProductList>>> getProductList(@Query("pageIndex") int pageIndex,
                                                               @Query("pageSize") int pageSize,
                                                               @Query("isShopShelves") boolean isShelves,
                                                               @Query("shopId") String shopId);


    /**
     * 根据商品id查询商品信息
     *
     * @param productId 商品id
     * @return
     */
    @GET(API.GET_PRODUCT_INFO_BY_PRODUCT_ID)
    Observable<BaseResponse<ProductInfoBean>> getProductInfo(@Query("productID") String productId);

    /**
     * 获取商品上架状况(上架和下架个数)
     *
     * @return
     */
    @GET(API.STATISTICS_PRODUCT_SHELVES_STATUS)
    Observable<BaseResponse<ProductShelvesStatistics>> getProductShelvesStatus();


    /**
     * 设置商品上架或下架
     *
     * @param requestBody
     * @return
     */
    @POST(API.SET_PRODUCT_IS_SHELVES)
    Observable<BaseResponse<Object>> setProductShelves(@Body JsonObject requestBody);

    /**
     * 获取商品分类列表
     *
     * @return
     */
    @GET(API.GET_CLASSIFY_LIST)
    Observable<BaseResponse<List<ProductClassifyListBean>>> getProductClassify();

    /**
     * 添加新的商品分类
     *
     * @param requestBody
     * @return
     */
    @POST(API.CREATE_CLASSIFY)
    Observable<BaseResponse<Object>> addProductClassify(@Body JsonObject requestBody);

    /**
     * 修改商品分类
     *
     * @param requestBody
     * @return
     */
    @POST(API.MODIFY_CLASSIFY)
    Observable<BaseResponse<Object>> modifyProductClassify(@Body JsonObject requestBody);

    /**
     * 删除商品分类
     *
     * @param requestBody
     * @return
     */
    @POST(API.DELETE_CLASSIFY)
    Observable<BaseResponse<Object>> deleteProductClassify(@Body JsonObject requestBody);

    /**
     * 获取运费模版list
     *
     * @return
     */
    @GET(API.GET_FREIGHT_LIST)
    Observable<BaseResponse<List<FreightListBean>>> getFreightTemplateList();

    /**
     * 删除运费模版
     *
     * @param requestBody
     * @return
     */
    @POST(API.DELETE_FREIGHT_TEMPLATE)
    Observable<BaseResponse<Object>> deleteFreightTemplate(@Body JsonObject requestBody);

    /**
     * 添加运费模版
     *
     * @param requestBody
     * @return
     */
    @POST(API.CREATE_FREIGHT_TEMPLATE)
    Observable<BaseResponse<Object>> addFreightTemplate(@Body JsonObject requestBody);

    /**
     * 修改运费模版
     *
     * @param requestBody
     * @return
     */
    @POST(API.MODIFY_FREIGHT_TEMPLATE)
    Observable<BaseResponse<Object>> modifyFreightTemplate(@Body JsonObject requestBody);


    /**
     * 删除规格
     *
     * @param requestBody
     * @return
     */
    @POST(API.DELETE_SPEC)
    Observable<BaseResponse<Object>> deleteSpecification(@Body JsonObject requestBody);

    /**
     * 获取规格列表
     *
     * @param productId 商品id
     * @return
     */
    @GET(API.GET_SPEC_LIST)
    Observable<BaseResponse<List<SpecificationListBea>>> getSpecificationList(@Query("productID") String productId);

    /**
     * 获取店铺钱包统计数据
     *
     * @param beginDate 开始时间
     * @param endDate   结束时间
     * @return
     */
    @GET(API.GET_SHOP_BALANCE_DETAILS_SUMMARY)
    Observable<BaseResponse<WalletSummaryBean>> getShopWalletSummaryData(@Query("beginDate") String beginDate,
                                                                         @Query("endDate") String endDate);


    /**
     * 获取店铺钱包余额
     *
     * @param shopId 店铺id
     * @return
     */
    @GET(API.GET_SHOP_BALANCE)
    Observable<BaseResponse<WalletBean>> getShopWalletBalance(@Query("shopId") String shopId);


    /**
     * 添加新的规格
     *
     * @param requestBody 携带的参数
     * @return
     */
    @POST(API.CREATE_SPEC)
    Observable<BaseResponse<Object>> addSpecification(@Body RequestBody requestBody);

    /**
     * 修改规格
     *
     * @param requestBody 携带的参数
     * @return
     */
    @POST(API.MODIFY_SPEC)
    Observable<BaseResponse<Object>> modifySpecification(@Body RequestBody requestBody);

    /**
     * 获取店铺会员数据
     *
     * @param requestBody
     * @return
     */
    @POST(API.GET_MY_SHOP_VIP_LIST)
    Observable<BaseResponse<ShopMemberBean>> getShopMemberData(@Body RequestBody requestBody);

    /**
     * 获取收银台列表
     *
     * @return
     */
    @GET(API.GET_SHOP_CASHIER_LIST)
    Observable<BaseResponse<List<CashierListBean>>> getCashierList();

    /**
     * 获取店铺钱包详情数据
     *
     * @param queryType 查询的类型(联盟收入、扫码订单等)
     * @param pageSize  每页包含的数据个数
     * @param pageIndex 页码
     * @param beginDate 起始时间
     * @param endDate   结束时间
     * @param isIncome  是否是收入明细
     * @param qrCodeId  收银台id
     * @return
     */
    @GET(API.GET_SHOP_WALLET_DATA_BY_CONDITION)
    Observable<BaseResponse<WalletShopOptionDetailsBean>> getShopWalletDetailsData(@Query("shopQueryType") int queryType,
                                                                                   @Query("pageSize") int pageSize,
                                                                                   @Query("pageIndex") int pageIndex,
                                                                                   @Query("beginDate") String beginDate,
                                                                                   @Query("endDate") String endDate,
                                                                                   @Query("isIncome") String isIncome,
                                                                                   @Query("shopQRCodeID") String qrCodeId);

    /**
     * 拼团列表
     */
    @POST(API.FIGHT_GROUP_LIST)
    Observable<BaseResponse<List<FightGroup>>> getFightGroupList(@Body JsonObject requestBody);

    /**
     * 查询聊天用户
     */
    @POST(API.SEARCHUSER)
    Observable<BaseResponse<List<SearchFriendBean.DataBean>>> searchUser(@Body JsonObject requestBody);

    /**
     * 查询聊天用户的token
     */
    @POST(API.FETCHLOGININFO)
    Observable<BaseResponse<ChatLoginBean.DataBean>> getChatToken();


    /**
     * 获取店铺二维码
     */
    @GET(API.MERCHANTPAYMENTCODE)
    Observable<BaseResponse<PaymentCodeBean.DataBean>> getQRCodeString(@Query("shopId") String shopId);

    /**
     * 获取物流公司列表
     */
    @GET(API.LOGISTICS_EXPRESS_COMPANY_LIST)
    Observable<BaseResponse<List<Express>>> getExpressCompanyList();


    /**
     * 获取店铺收银台列表
     */
    @GET(API.GETCASHIERS)
    Observable<BaseResponse<List<CashierBean.DataBean>>> getCasher(@Query("shopId") String shopId);

    /**
     * 增加店铺收银台列表
     */
    @GET(API.GENERATE_CASHIER)
    Observable<BaseResponse<Object>> generateCashier(@Query("remark") String name, @Query("shopId") String shopId);


    /**
     * 根据手机号码返回受邀请收银员信息
     */
    @GET(API.QUERY_CASHIER)
    Observable<BaseResponse<AddCashierBean.DataBean>> getInviteInfo(@Query("shopId") String shopId, @Query("mobile") String mobile);

    /**
     * 推信息
     */
    @POST(API.PUSHMSGCONNECTIONSENDMSG)
    Observable<BaseResponse<Object>> getSendMsg(@Body Map<String, String> body);

    /**
     * 修改登录密码
     */
    @POST(API.MODIFY_LOGIN_PASSWORD)
    Observable<BaseResponse<Object>> modifyLoginPassword(@Body JsonObject jsonObject);

    /**
     * 获取短信验证码
     *
     * @param mobile  手机号码
     * @param ApiName 固定参数EditAccountCashPassword
     */
    @FormUrlEncoded
    @POST(API.SMS_CODE)
    Observable<BaseResponse<String>> getSmsCode(@Field("LoginName") String mobile, @Field("ApiName") String ApiName);

    /**
     * 设置支付密码
     *
     * @param oldPwd        当设置密码时传空字符串，修改密码时传原密码
     * @param newPwd        新密码
     * @param newPwdConfirm 确认新密码
     * @param smsCode       验证码
     * @param apiName       固定参数EditAccountCashPassword
     */
    @FormUrlEncoded
    @POST(API.PASSWORD_PAYMENT_SETTING)
    Observable<BaseResponse<Object>> setPayPassword(@Field("OldCashPasswordCode") String oldPwd
            , @Field("CashPasswordCode") String newPwd
            , @Field("CashSurePassword") String newPwdConfirm
            , @Field("VerifyCode") String smsCode
            , @Field("ApiName") String apiName
    );

    /**
     * 收货地址列表
     */
    @POST(API.LOGISTICS_ADDRESS_LIST)
    Observable<BaseResponse<List<Address>>> getAddressList();

    /**
     * 编辑收货地址
     */
    @POST(API.LOGISTICS_ADDRESS_EDIT)
    Observable<BaseResponse<Object>> editAddress(@Body JsonObject requestBody);

    /**
     * 添加收货地址
     */
    @POST(API.LOGISTICS_ADDRESS_ADD)
    Observable<BaseResponse<Object>> addAddress(@Body JsonObject requestBody);
package td.com.xiaoheixiong.api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import td.com.xiaoheixiong.beans.BaseResponse;
import td.com.xiaoheixiong.beans.MyAccount.MyAccountData;
import td.com.xiaoheixiong.beans.MyMember.MyMemberData;
import td.com.xiaoheixiong.beans.earns.MyEarnsData;


public interface APIService {


    /**
     * 我的会员
     * @param mer_id
     * @return
     */
    @POST("api/getFlowList")
    @FormUrlEncoded
    Call<BaseResponse<MyMemberData>> getFlowList(@Field("mer_id") String mer_id);


    /**
     * 代理商收益
     * @param mer_id
     * @return
     */
    @POST("api/getMyWalletList")
    @FormUrlEncoded
    Call<BaseResponse<MyEarnsData>> getMyWalletList(@Field("mer_id") String mer_id);

    /**
     * 我的钱包
     * @param mer_id
     * @return
     */
    @POST("api/getMyMerchantWalletList")
    @FormUrlEncoded
    Call<BaseResponse<MyAccountData>> getMyMerchantWalletList(@Field("mer_id") String mer_id,@Field("type") int type);

}

package td.com.xiaoheixiong.fragments.Merchants;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.activity.ViewPagerMainActivity;
import td.com.xiaoheixiong.fragments.BaseFragment;

@SuppressLint("NewApi")
public class MerchartDFragment extends BaseFragment {


    @Bind(R.id.go_btn)
    Button goBtn;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_merchart_d, null);
        ButterKnife.bind(this, view);

        return view;
    }

    private void initview() {


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.go_btn)
    public void onViewClicked() {
        Intent it = new Intent(getActivity(), ViewPagerMainActivity.class);
        getActivity().finish();
    }
}

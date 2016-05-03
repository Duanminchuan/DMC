package com.dream.cutepet.fragment;

import com.dream.cutepet.R;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * 首页
 * 
 * @author Administrator
 * 
 */
public class HomePageAndPetStrategyFragment extends Fragment {

	HomePageFragment homePageFragmentChen;
	PetStrategyFragment petStrategyFragment;

	RadioButton resources, strategy;
	RadioGroup radiogroup_homepage;

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_homepage_pet_strategy, null);

		resources = (RadioButton) view.findViewById(R.id.resources);
		strategy = (RadioButton) view.findViewById(R.id.strategy);

		radiogroup_homepage = (RadioGroup) view.findViewById(R.id.radiogroup_homepage);
		homePageFragmentChen = new HomePageFragment(getActivity());
		petStrategyFragment = new PetStrategyFragment(getActivity());
		initView();
		radiogroup_homepage.setOnCheckedChangeListener(checkedChangeListener);
		return view;
	}

	private void initView() {
		resources.setChecked(true);
		strategy.setChecked(false);
		FragmentManager fm = getFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		transaction.replace(R.id.mid_homepage_petstrategy, homePageFragmentChen);
		transaction.commit();
	}

	OnCheckedChangeListener checkedChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			FragmentManager fm = getFragmentManager();
			FragmentTransaction transaction = fm.beginTransaction();
			switch (checkedId) {
			case R.id.resources:
				resources.setChecked(true);
				strategy.setChecked(false);
				if (homePageFragmentChen == null) {
					homePageFragmentChen = new HomePageFragment();
				}
				transaction.replace(R.id.mid_homepage_petstrategy, homePageFragmentChen);
				break;
			case R.id.strategy:
				strategy.setChecked(true);
				resources.setChecked(false);
				if (petStrategyFragment == null) {
					petStrategyFragment = new PetStrategyFragment();
				}
				transaction.replace(R.id.mid_homepage_petstrategy, petStrategyFragment);
				break;
			default:
				break;
			}
			transaction.commit();
		}
	};
	/**
	 * 点击顶部切换页面
	 */
	OnClickListener clickListener = new OnClickListener() {
		@SuppressLint({ "InflateParams", "Recycle" })
		public void onClick(View v) {
			FragmentManager fm = getFragmentManager();
			FragmentTransaction transaction = fm.beginTransaction();
			switch (v.getId()) {
			case R.id.resources:
				resources.setChecked(true);
				strategy.setChecked(false);
				if (homePageFragmentChen == null) {
					homePageFragmentChen = new HomePageFragment();
				}
				transaction.replace(R.id.mid_homepage_petstrategy, homePageFragmentChen);
				break;
			case R.id.strategy:
				strategy.setChecked(true);
				resources.setChecked(false);
				if (petStrategyFragment == null) {
					petStrategyFragment = new PetStrategyFragment();
				}
				transaction.replace(R.id.mid_homepage_petstrategy, petStrategyFragment);
				break;
			default:
				break;
			}
			transaction.commit();
		}
	};

}

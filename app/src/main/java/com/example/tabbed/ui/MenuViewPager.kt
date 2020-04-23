package com.example.tabbed.ui

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.tabbed.Model.MenuModel

import com.example.tabbed.R
import com.example.tabbed.Util.CustomViewPager
import com.example.tabbed.Util.MenuRecyclerViewClickListener
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.list_menu.*

class MenuViewPager(private val listener: MenuRecyclerViewClickListener) : Fragment(), MenuRecyclerViewClickListener {
    private var tabLayout: TabLayout? = null
    private var viewPager: CustomViewPager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu_view_pager, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewPager = (R.id.viewPagerInMenuView) as CustomViewPager
        viewPager = view!!.findViewById(R.id.viewPagerInMenuView) as CustomViewPager
        setupViewPager(viewPager!!)
        viewPager!!.setSwipePagingEnabled(false)
        viewPager!!.setOffscreenPageLimit(3)

        tabLayout = view!!.findViewById(R.id.tabs) as TabLayout
        tabLayout!!.setupWithViewPager(viewPager)



    }

    internal inner class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        private val mFragmentList = ArrayList<Fragment>()
        private val mFragmentTitleList = ArrayList<String>()

        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence {
            return mFragmentTitleList[position]
        }
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(FragOne(this), "MAIN COURSES")
        adapter.addFragment(FragTwo(this), "SALADS")
        adapter.addFragment(FragThree(this), "DESSERTS")
        adapter.addFragment(FragFour(this), "DRINKS")
        viewPager.adapter = adapter
    }

    override fun menuOnClick(view: View, menuData: MenuModel, quantity: Int) {
        when(view.id){
            R.id.imgFood -> {
                listener.menuOnClick(imgFood, menuData, quantity)
            }
        }
    }

}

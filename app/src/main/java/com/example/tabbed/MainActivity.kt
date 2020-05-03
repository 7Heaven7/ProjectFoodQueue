package com.example.tabbed

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.tabbed.Backend.SharedPrefManager
import com.example.tabbed.Model.MenuModel
import com.example.tabbed.Model.UserModel
import com.example.tabbed.Util.CustomViewPager
import com.example.tabbed.Util.MenuRecyclerViewClickListener
import com.example.tabbed.ViewModel.AdminQueueViewModel
import com.example.tabbed.ui.*
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), MenuRecyclerViewClickListener {
    private val TAG = "TAGMainActivity"
    private var tabLayout: TabLayout? = null
    private var viewPager: CustomViewPager? = null
    private var user: UserModel? = null

    lateinit var userRole: String

    private val fragmentManager = supportFragmentManager
    private val fragmentOrder = FragOrderList()
    private val fragmentAddEditMenu = FragAddEditMenu(this)
    private val fragmentTabLayout = MenuViewPager(this)
    private val fragmentAdminMiniOrder = FragAdminMiniOrder()
    lateinit var adminQueueViewModel: AdminQueueViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (SharedPrefManager.getInstance(this).isLoggedIn){
            user = SharedPrefManager.getInstance(this).getUser
            userRole = user!!.role
            txtRole.text = user!!.role
            if (userRole == "Customer") txtTable.text = "Table : ${user!!.user}"
            else {
                //fabAddEditMenu.show()
                txtTable.text = "${user!!.user}"
                btnConfirmMain.visibility = View.GONE
                btnBillMain.visibility = View.VISIBLE
            }
        }

        val fragmentTransaction = fragmentManager.beginTransaction()
        if (userRole == "Customer") fragmentTransaction.replace(R.id.orderlayout, fragmentOrder)
        else fragmentTransaction.replace(R.id.orderlayout, fragmentAdminMiniOrder)
        fragmentTransaction.replace(R.id.tabberlayout, fragmentTabLayout)
        fragmentTransaction.commit()

        setupButton()

/*        adminQueueViewModel = ViewModelProviders.of(this).get(AdminQueueViewModel::class.java)
        adminQueueViewModel.init()
        adminQueueViewModel.getAdminQueueVM().observe(this, Observer{
            txtCountQueueMain.text = it.orderlist.size.toString()
        })*/
    }

    private fun setupButton(){
        btnConfirmMain.setOnClickListener {
            if (userRole == "Customer"){
                val intent = Intent(applicationContext, DetailActivity::class.java)
                intent.putExtra("FRAGMENT_CODE", "ConfirmOrder")
                startActivity(intent)
            }
        }
        btnBillMain.setOnClickListener {
            if (userRole == "Manager" || userRole == "Waiter"){
                val intent = Intent(applicationContext, DetailActivity::class.java)
                intent.putExtra("FRAGMENT_CODE", "AdminGetBill")
                startActivity(intent)
            }
        }
        btnQueueMain.setOnClickListener {
            if (userRole == "Customer"){
                val intent = Intent(applicationContext, DetailActivity::class.java)
                intent.putExtra("FRAGMENT_CODE", "CustomerSeeQueue")
                startActivity(intent)
            }
            else {
                val intent = Intent(applicationContext, DetailActivity::class.java)
                intent.putExtra("FRAGMENT_CODE", "AdminSeeQueue")
                startActivity(intent)
            }
        }
        userlayout.setOnLongClickListener {
            if (userRole != "Customer") {
                val dialogBuilder = AlertDialog.Builder(this)
                dialogBuilder.setMessage(it.toString())
                    // if the dialog is cancelable
                    .setCancelable(false)
                    .setPositiveButton("Yes", DialogInterface.OnClickListener { _, _ ->
                        SharedPrefManager.getInstance(applicationContext).logoutUser()
                        val intent = Intent(applicationContext, StartPage::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    })
                    .setNegativeButton("No", DialogInterface.OnClickListener { dialog, _ ->
                        dialog.dismiss()
                    })

                val alert = dialogBuilder.create()
                alert.setTitle("LOGOUT")
                alert.setMessage("Are you sure?")
                alert.show()
                true
            }
            true
        }
        fabAddEditMenu.setOnClickListener{
            if (userRole == "Manager"){
                //fabAddEditMenu.hide()
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.replace(R.id.tabberlayout, fragmentAddEditMenu)
                fragmentTransaction.commit()
            }
        }
        onBackPressed()
    }

    fun hideFloatingActionButton(){ fabAddEditMenu.hide()}
    fun showFloatingActionButton(){ fabAddEditMenu.show()}

    override fun menuOnClick(view: View, menuData: MenuModel, quantity: Int) {
        when(view.id){
            R.id.imgFood -> {
                val bundle = Bundle()
                bundle.putInt("id_menu", menuData.id_menu)
                bundle.putString("menu_name", menuData.menu_name)
                bundle.putString("image_file", menuData.image_file)
                bundle.putString("type", menuData.type)
                bundle.putInt("price", menuData.price)
                fragmentAddEditMenu.arguments = bundle
    
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.replace(R.id.tabberlayout, fragmentAddEditMenu)
                fragmentTransaction.commit()
            }
            R.id.imgBackButtonEditMenu -> {
                onBackPressed()
            }
        }
    }

    override fun onBackPressed() { }

}
package com.ruczajsoftware.workoutrival.ui.dashboard

import android.content.DialogInterface
import com.ruczajsoftware.workoutrival.R
import com.ruczajsoftware.workoutrival.util.areYouSureDialog
import com.ruczajsoftware.workoutrival.util.base.BaseActivity
import kotlin.system.exitProcess

class DashboardActivity : BaseActivity() {
    override val contentViewLayout = R.layout.activity_dashboard
    override fun displayProgressBar(bool: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun afterViews() {}

    override fun onBackPressed() {
        areYouSureDialog("Do you want exit?",
            DialogInterface.OnClickListener { _, _ ->
                finishAffinity()
                exitProcess(0)
            })
    }
}
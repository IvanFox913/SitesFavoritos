package br.edu.ifsp.dmo.sitesfavoritos.view

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifsp.dmo.sitesfavoritos.R
import br.edu.ifsp.dmo.sitesfavoritos.databinding.ActivityMainBinding
import br.edu.ifsp.dmo.sitesfavoritos.databinding.SitesDialogBinding
import br.edu.ifsp.dmo.sitesfavoritos.model.Site
import br.edu.ifsp.dmo.sitesfavoritos.view.adapters.SiteAdapter
import br.edu.ifsp.dmo.sitesfavoritos.view.listeners.ISiteItemClickListener

class MainActivity : AppCompatActivity(), ISiteItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private var datasource = ArrayList<Site>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configListeners()
        configRecyclerView()
    }

    override fun clickSiteItem(position: Int) {
        val site = datasource[position]
        val mIntent = Intent(Intent.ACTION_VIEW)
        mIntent.setData(Uri.parse("http://" + site.url))
        startActivity(mIntent)
    }

    override fun clickHeartSiteItem(position: Int) {
        val site = datasource[position]
        site.favorite = !site.favorite
        notifyAdapter()
    }

    override fun clickDeleteSiteItem(position: Int) {
        val site = datasource[position]
        handleDeleteSite(site)
        notifyAdapter()
    }

    private fun configListeners() {
        binding.buttonAdd.setOnClickListener { handleAddSite() }
    }

    private fun configRecyclerView() {
        val adapter = SiteAdapter(this, datasource, this)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)

        binding.recyclerviewSites.layoutManager = layoutManager
        binding.recyclerviewSites.adapter = adapter
    }

    private fun notifyAdapter() {
        val adapter = binding.recyclerviewSites.adapter
        adapter?.notifyDataSetChanged()
    }

    private fun handleAddSite() {
        val window = layoutInflater.inflate(R.layout.sites_dialog, null)
        val bindingDialog: SitesDialogBinding = SitesDialogBinding.bind(window)
        val builder = AlertDialog.Builder(this)
            .setView(window)
            .setTitle(R.string.new_site)
            .setPositiveButton(R.string.save,
                DialogInterface.OnClickListener { dialog, which ->
                    datasource.add(
                        Site(
                            bindingDialog.edittextNickname.text.toString(),
                            bindingDialog.edittextUrl.text.toString()
                        )
                    )
                    notifyAdapter()
                    dialog.dismiss()
                })
            .setNegativeButton(R.string.cancel,
                DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                })
        val dialog = builder.create()
        dialog.show()
    }

    private fun handleDeleteSite(site: Site) {
        val window = layoutInflater.inflate(R.layout.delete_site_dialog, null)
        val builder = AlertDialog.Builder(this)
            .setView(window)
            .setTitle(R.string.delete_site)
            .setPositiveButton(R.string.delete,
                DialogInterface.OnClickListener { dialog, which ->
                    datasource.remove(site)
                    notifyAdapter()
                    dialog.dismiss()
                })
            .setNegativeButton(R.string.cancel,
                DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                })
        val dialog = builder.create()
        dialog.show()
    }
}
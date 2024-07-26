package br.edu.ifsp.dmo.sitesfavoritos.view.listeners

interface ISiteItemClickListener {

    fun clickSiteItem(position: Int)

    fun clickHeartSiteItem(position: Int)

    fun clickDeleteSiteItem(position: Int)

}
package com.teufelsturm.tt_downloader_kotlin.data.entity

interface SummitInterface : SummitBaseDataInterface {
    val intAnzahlSternchenWege: Int?
    val intAnzahlWege: Int?
    val strLeichtesterWeg: String?
}


interface SummitBaseDataInterface {
    val strName: String?
    val strGebiet: String?
    val intKleFuGipfelNr: Int?
}
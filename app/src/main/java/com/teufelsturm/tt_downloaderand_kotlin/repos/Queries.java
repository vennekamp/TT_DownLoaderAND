package com.teufelsturm.tt_downloader3.repos;

import android.content.Context;
import android.database.DatabaseUtils;
import android.util.Log;

import com.teufelsturm.tt_downloader3.R;

import org.jetbrains.annotations.NotNull;

public class Queries {
    private final static String TAG = Queries.class.getSimpleName();

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //    Comments /////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    public static String getSQL4CommentsSearch(@NotNull Context context, String strTextSuchtext,
                                               @NotNull String strGebiet, int intMinSchwierigkeit,
                                               int intMaxSchwierigkeit, int intMinGradingOfComment) {
//			SELECT a.[intTTWegNr], a.[strEntryKommentar], b.[WegName], b.[strSchwierigkeitsGrad], c.[strName],  b.[intTTGipfelNr], a.[entryBewertung], a.[strEntryUser], a.[entryDatum]
//				      FROM  [TT_Summit_AND] c, [TT_Route_AND] b, [TT_RouteComment_AND] a
//				      WHERE a.[entryBewertung] >= 0
//				      AND a.[strEntryKommentar] like '%%'
//				      AND a.[intTTWegNr] = b.[intTTWegNr]
//				      AND c.[strGebiet] = 'Bielatal'
//				      AND c.[intTTGipfelNr] = b.[intTTGipfelNr]
//				      AND coalesce(b.[sachsenSchwierigkeitsGrad],
//							b.[ohneUnterstützungSchwierigkeitsGrad],
//							b.[rotPunktSchwierigkeitsGrad]
//							, b.[intSprungSchwierigkeitsGrad] ) BETWEEN 1 AND 15
//				     ORDER BY b.[intTTGipfelNr] LIMIT 250
        String queryString1;
        queryString1 = new StringBuilder()
                .append("SELECT a.[intTTWegNr], a.[strEntryKommentar], b.[WegName], b.[strSchwierigkeitsGrad]")
                .append(" , c.[strName], ").append("b.[intTTGipfelNr], a.[entryBewertung], a.[strEntryUser], a.[entryDatum]")
                .append("      FROM  [TT_Summit_AND] c, [TT_Route_AND] b, [TT_RouteComment_AND] a")
                .append("      WHERE a.[entryBewertung] >= ").append(intMinGradingOfComment)
                .append("      AND a.[strEntryKommentar] like ")
                .append(DatabaseUtils.sqlEscapeString("%" + strTextSuchtext + "%"))
                .append("     AND a.[intTTWegNr] = b.[intTTWegNr]")
                .append(strGebiet.equals(context.getString(R.string.strAll))
                        ? "       AND c.[strGebiet] != \"\" ": "       AND c.[strGebiet] = '" + strGebiet + "'")
                .append("      AND c.[intTTGipfelNr] = b.[intTTGipfelNr]")
                .append("      AND coalesce(b.[sachsenSchwierigkeitsGrad],")
                .append("			b.[ohneUnterstützungSchwierigkeitsGrad],")
                .append("			b.[rotPunktSchwierigkeitsGrad]")
                .append("			, b.[intSprungSchwierigkeitsGrad] ) ")
                .append(" BETWEEN ").append(intMinSchwierigkeit)
                .append(" AND ").append(intMaxSchwierigkeit)
                .append("     ORDER BY c.[strName] LIMIT ")
                .append(context.getResources().getInteger(R.integer.MaxNoItemQuerxy)).toString();
        Log.i(TAG,"Neue Kommentarr zum Suche finden:\r\n" + queryString1);
        return queryString1;
    }

    @NotNull
    public static String getSQL4CommentsBySummit(int intTTGipfelNr){
        // TODO: MOVE to ROOM
        return new StringBuilder()
                .append("SELECT a.[strName], a.[strGebiet]  ")
                .append(" from [TT_Summit_AND] a where a.[intTTGipfelNr]  = ")
                .append(intTTGipfelNr).toString();
    }


    @NotNull
    public static String getSQL4CommentsByRoute(int intTTWegNr){
        return new StringBuilder()
                .append("SELECT a.[intTTWegNr], a.[strEntryKommentar], a.[entryBewertung] ")
                .append(", a.[strEntryUser], a.[entryDatum] ")
                .append("from [TT_RouteComment_AND] a where a.[intTTWegNr]  = ")
                .append(intTTWegNr)
                .append(" ORDER BY a.[entryDatum] ASC ").toString();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //    Route ////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @NotNull
    public static String getSQL4RoutesSearch(@NotNull Context context, int intMinAnzahlDerKommentare,
                                             float floatMittlereWegBewertung, String strTextSuchtext,
                                             @NotNull String strGebiet, int intMinSchwierigkeit,
                                             int intMaxSchwierigkeit) {
        Log.i(TAG, "queryString erzeugen... ");
        //// Select All Query
//			 SELECT a.[intTTWegNr], a.[intTTGipfelNr]
//			 , a.[WegName], c.[strName], a.[blnAusrufeZeichen]
//			 , a.[intSterne], a.[strSchwierigkeitsGrad],
//			 a.[intSprungSchwierigkeitsGrad]
//			 , a.[sachsenSchwierigkeitsGrad]
//			 , a.[ohneUnterstützungSchwierigkeitsGrad]
//			 , a.[rotPunktSchwierigkeitsGrad]
//			 , a.[intSprungSchwierigkeitsGrad]
//			 , a. [intAnzahlDerKommentare], a. [fltMittlereWegBewertung]
//			 , d.[isAscendedRoute], d.[intDateOfAscend], d.[strMyRouteComment]
//			 FROM [TT_Route_AND] a, [TT_Summit_AND] c
//			 LEFT OUTER JOIN [myTT_Route_AND] d
//			 ON a.[intTTWegNr] = d.[intTTWegNr]
//			 WHERE a.[intAnzahlDerKommentare] >= 1
//			 AND a.[fltMittlereWegBewertung] >= -3
//			 AND a.[WegName] LIKE '%AW direkt%'
//			 AND a.[intTTGipfelNr] in (
//			 SELECT DISTINCT b.[intTTKletterWeg4Gipfel] from
//			 [TT_Route4SummitAND] b
//			 WHERE b.[intTTHauptGipfelNr] in (
//			 SELECT DISTINCT c.[intTTGipfelNr] from [TT_Summit_AND] c
//			 where c.[strGebiet] != ""
//			 )
//			 )
//			 AND a.[intTTGipfelNr] = c.[intTTGipfelNr]
//			 AND coalesce(a.[sachsenSchwierigkeitsGrad],
//			 a.[ohneUnterstützungSchwierigkeitsGrad],
//			 a.[rotPunktSchwierigkeitsGrad]
//			 , a.[intSprungSchwierigkeitsGrad] ) BETWEEN 1 AND 15
        String queryString = new StringBuilder()
                .append("SELECT a.[intTTWegNr], a.[intTTGipfelNr]\r\n")
                .append("       , a.[WegName], c.[strName], a.[blnAusrufeZeichen]\r\n")
                .append("       , a.[intSterne], a.[strSchwierigkeitsGrad], a.[intSprungSchwierigkeitsGrad]\r\n")
                .append("       , a.[sachsenSchwierigkeitsGrad]\r\n")
                .append("       , a.[ohneUnterstützungSchwierigkeitsGrad]\r\n")
                .append("       , a.[rotPunktSchwierigkeitsGrad]\r\n")
                .append("       , a.[intSprungSchwierigkeitsGrad]\r\n")
                .append("       , a. [intAnzahlDerKommentare], a. [fltMittlereWegBewertung]\r\n")
                .append("       , d.[isAscendedRoute], d.[intDateOfAscend], d.[strMyRouteComment]\r\n")
                .append("       FROM [TT_Route_AND] a, [TT_Summit_AND] c\r\n")
                .append("       LEFT OUTER JOIN [myTT_Route_AND] d\r\n")
                .append("            ON a.[intTTWegNr] = d.[intTTWegNr]\r\n")
                .append("       WHERE a.[intAnzahlDerKommentare] >= ")
                .append(intMinAnzahlDerKommentare)
                .append("\r\n       AND a.[fltMittlereWegBewertung] >= ")
                .append(floatMittlereWegBewertung)
                .append("\r\n       AND a.[WegName] LIKE ")
                .append(DatabaseUtils.sqlEscapeString("%" + strTextSuchtext + "%"))
                .append(strGebiet.equals(context.getString(R.string.strAll)) ? "       AND c.[strGebiet] != \"\" "
                        : "       AND c.[strGebiet] = '" + strGebiet + "'")
                .append("\r\n		  AND a.[intTTGipfelNr] in (\r\n")
                .append("       SELECT DISTINCT b.[intTTKletterWeg4Gipfel] from [TT_Route4SummitAND] b\r\n")
                .append("       WHERE b.[intTTHauptGipfelNr] in (\r\n")
                .append("       	   		SELECT DISTINCT c.[intTTGipfelNr] from [TT_Summit_AND] c)\r\n")
                .append("           )\r\n")
                .append("\r\n 	  AND a.[intTTGipfelNr] = c.[intTTGipfelNr]")
                .append("\r\n       AND  coalesce(a.[sachsenSchwierigkeitsGrad], a.[ohneUnterstützungSchwierigkeitsGrad], " +
                        "a.[rotPunktSchwierigkeitsGrad]")
                .append("              , a.[intSprungSchwierigkeitsGrad] ) BETWEEN ")
                .append(intMinSchwierigkeit).append(" AND ")
                .append(intMaxSchwierigkeit)
                .append(" ORDER BY WegName")
                .append(" Limit ")
                .append(context.getResources().getInteger(R.integer.MaxNoItemQuerxy)).toString();
        Log.i(TAG, "Neue Query erzeugt..."
                + queryString);
        return queryString;
    }

    @NotNull
    public static String getSQL4RouteObject(int intTTWegNr){
        return new StringBuilder()
                .append("SELECT a.[intTTWegNr], a.[intTTGipfelNr], a.[WegName], c.[strName]")
                .append(", a.[blnAusrufeZeichen], a.[intSterne], a.[strSchwierigkeitsGrad]")
                .append(", a.[sachsenSchwierigkeitsGrad], a.[ohneUnterstützungSchwierigkeitsGrad]")
                .append(", a.[rotPunktSchwierigkeitsGrad], a.[intSprungSchwierigkeitsGrad]")
                .append(", a.[intAnzahlDerKommentare], a.[fltMittlereWegBewertung]")
                .append(", b.[isAscendedRoute], b.[intDateOfAscend], b.[strMyRouteComment]")
                .append(" FROM [TT_Summit_AND] c, TT_Route_AND a")
                .append(" LEFT OUTER JOIN myTT_Route_AND b")
                .append(" ON (a.[intTTWegNr] = b.[intTTWegNr])")
                .append("WHERE a.[intTTGipfelNr] = c.[intTTGipfelNr]")
                .append("AND a.[intTTWegNr] = ")
                .append(intTTWegNr).toString();
    }

    @NotNull
    public static String getSQL4RoutesBySummit(int intTTGipfelNr){
        return new StringBuilder()
                .append("SELECT a.intTTWegNr, a.WegName, a.strSchwierigkeitsGrad")
                .append(", a.intSterne, a.blnAusrufeZeichen, a.sachsenSchwierigkeitsGrad")
                .append(", a.ohneUnterstützungSchwierigkeitsGrad")
                .append(", a.rotPunktSchwierigkeitsGrad, a.intSprungSchwierigkeitsGrad")
                .append(", a.intAnzahlDerKommentare, a.fltMittlereWegBewertung")
                .append(", b.[isAscendedRoute], b.[intDateOfAscend], b.[strMyRouteComment]")
                .append(" FROM TT_Route_AND a")
                .append(" LEFT OUTER JOIN myTT_Route_AND b")
                .append(" ON (a.[intTTWegNr] = b.[intTTWegNr])")
                .append(" WHERE a.[intTTGipfelNr] = ")
                .append(intTTGipfelNr) // aTT_Summit_AND.getIntTT_IDOrdinal()
                .append(" ORDER BY a.WegName").toString();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //    Summits //////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @NotNull
    public static String getSQL4SummitsSearch(String strTextSuchtext, @NotNull String strGebiet,
                                              int intMinAnzahlDerWege, int intMaxAnzahlDerWege,
                                              int intMinAnzahlDerSternchenWege, int intMaxAnzahlDerSternchenWege) {
        String queryString;
        Log.i(TAG, "queryString erzeugen... ");
        queryString = new StringBuilder().append("SELECT   a.intTTGipfelNr, a.strName, a.strGebiet")
                .append(", a.intKleFuGipfelNr , a.intAnzahlWege , a.intAnzahlSternchenWege")
                .append(", a.strLeichtesterWeg, a.dblGPS_Latitude, a.dblGPS_Longitude")
                .append(", b.[isAscendedSummit], b.[intDateOfAscend], b.[strMySummitComment] ")
                .append(" FROM ")
                .append(" TT_Summit_AND a ")
                .append(" LEFT OUTER JOIN myTT_Summit_AND b")
                .append(" ON (a.[intTTGipfelNr] = b.[intTTGipfelNr]) ")
                .append(" WHERE ").toString();
        Log.i(TAG, "queryString erweitern (1)... " + strTextSuchtext);
        if (!strGebiet.equals("Alle")){
            queryString += " strGebiet = '" + strGebiet + "' AND ";
        }
        Log.i(TAG, "queryString erweitern (2)... ");
        queryString += " intAnzahlWege >= "
                + intMinAnzahlDerWege + " AND intAnzahlWege <= '"
                + intMaxAnzahlDerWege
                + "' AND intAnzahlSternchenWege >= '"
                + intMinAnzahlDerSternchenWege
                + "' AND intAnzahlSternchenWege <= '"
                + intMaxAnzahlDerSternchenWege + "'";
        Log.i(TAG, "queryString erweitern (3)... ");
        if (!"".equals(strTextSuchtext)) {
            queryString += " AND strName like '%"
                    + strTextSuchtext + "%'";
        }
        queryString += " LIMIT 10;";
        Log.i(TAG, "Neuen Gipfel suchen:\r\n" + queryString);
        return queryString;
    }

    @NotNull
    public static String getSQL4SummitObject(int intTTGipfelNr){
        return new StringBuilder()
                .append("SELECT   a.intTTGipfelNr, a.strName, a.strGebiet")
                .append(", a.intKleFuGipfelNr , a.intAnzahlWege , a.intAnzahlSternchenWege")
                .append(", a.strLeichtesterWeg, a.dblGPS_Latitude, a.dblGPS_Longitude")
                .append(", b.[isAscendedSummit], b.[intDateOfAscend], b.[strMySummitComment] ")
                .append(" FROM ")
                .append(" TT_Summit_AND a ")
                .append(" LEFT OUTER JOIN myTT_Summit_AND b")
                .append(" ON (a.[intTTGipfelNr] = b.[intTTGipfelNr]) ")
                .append(" WHERE a.[intTTGipfelNr] = ")
                .append(intTTGipfelNr).toString();
    }


    @NotNull
    public static String getSQL4SummitsAscension(int intTTGipfelNr){
        return new StringBuilder("SELECT a.[isAscendedSummit], a.[intDateOfAscend], a.[strMySummitComment] ")
                .append("FROM [myTT_Summit_AND] a").append(" WHERE a.[intTTGipfelNr] =  ")
                .append(intTTGipfelNr).toString();
    }

    @NotNull
    public static String getSQL4SummitNeighbours(int intTTGipfelNr){
        return new StringBuilder()
                .append("SELECT a.[intTTGipfelNr], a.[strName], a.[dblGPS_Latitude], a.[dblGPS_Longitude]")
                .append(" from [TT_Summit_AND] a")
                .append(" where a.[intTTGipfelNr] in")
                .append("		 ( SELECT b.[intTTNachbarGipfelNr]")
                .append(" 		from [TT_NeigbourSummit_AND] b")
                .append(" 		where b.[intTTHauptGipfelNr] = ")
                .append(intTTGipfelNr)
                .append(" )").toString();
    }

}

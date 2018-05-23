///*
// * Copyright 2014 Ludwig M Brinckmann
// * Copyright 2015-2017 devemux86
// *
// * This program is free software: you can redistribute it and/or modify it under the
// * terms of the GNU Lesser General Public License as published by the Free Software
// * Foundation, either version 3 of the License, or (at your option) any later version.
// *
// * This program is distributed in the hope that it will be useful, but WITHOUT ANY
// * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
// * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
// *
// * You should have received a copy of the GNU Lesser General Public License along with
// * this program. If not, see <http://www.gnu.org/licenses/>.
// */
//package com.teufelsturm.tt_downloaderand_kotlin.gpslocation;
//
//import android.os.Bundle;
//
//import com.teufelsturm.tt_downloaderand_kotlin.R;
//
//import org.mapsforge.map.layer.renderer.TileRendererLayer;
//import org.mapsforge.map.rendertheme.InternalRenderTheme;
//import org.mapsforge.map.rendertheme.XmlRenderTheme;
//
///**
// * The simplest form of creating a map viewer based on the MapViewerTemplate.
// * It also demonstrates the use simplified cleanup operation at activity exit.
// */
//public class SimplestMapViewer extends MapViewerTemplate {
//
//    private static final String TAG = SimplestMapViewer.class.getSimpleName();
//
//    /**
//     * This MapViewer uses the built-in Osmarender theme.
//     *
//     * @return the render theme to use
//     */
//    @Override
//    protected XmlRenderTheme getRenderTheme() {
//        return InternalRenderTheme.OSMARENDER;
//    }
//
//    /**
//     * This MapViewer uses the standard xml layout in the Samples app.
//     */
//    @Override
//    protected int getLayoutId() {
//        return R.layout.mapviewer;
//    }
//
//    /**
//     * The id of the mapview inside the layout.
//     *
//     * @return the id of the MapView inside the layout.
//     */
//    @Override
//    protected int getMapViewId() {
//        return R.id.mapView;
//    }
//
//    /**
//     * The name of the map file.
//     *
//     * @return map file name
//     */
//    @Override
//    protected String getMapFileName() {
//        return "saechsische_schweiz.map";
//    }
//
//    /**
//     * Creates a simple tile renderer layer with the AndroidUtil helper.
//     */
//    @Override
//    protected void createLayers() {
//        TileRendererLayer tileRendererLayer = MapUtils.createTileRendererLayer(this.tileCaches.get(0),
//                this.mapView.getModel().mapViewPosition,
//                getMapFile(),
//                getRenderTheme(), false, true, false);
//        this.mapView.getLayerManager().getLayers().add(tileRendererLayer);
//    }
//
//    @Override
//    protected void createMapViews() {
//        super.createMapViews();
//    }
//
//    /**
//     * Creates the tile cache with the AndroidUtil helper
//     */
//    @Override
//    protected void createTileCaches() {
//        this.tileCaches.add(MapUtils.createTileCache(this, getPersistableId(),
//                this.mapView.getModel().displayModel.getTileSize(), this.getScreenRatio(),
//                this.mapView.getModel().frameBufferModel.getOverdrawFactor()));
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        // 1st of all: we need to check for the MAP_FILE
//        MapUtils.checkOrCopyMapFile(this);
//        super.onCreate(savedInstanceState);
//        setTitle(TAG);
//    }
//}

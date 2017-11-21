package com.restamenu.loader;

import android.content.Context;

import com.restamenu.api.ApiFactory;
import com.restamenu.api.RestaMenuService;

/**
 * @author Roodie
 */

public class ProfileLoader extends BaseLoader {
    private final RestaMenuService service;

    public ProfileLoader(Context context) {
        super(context);
        this.service = ApiFactory.getService();
    }

    /**
     * Override this, to implement request
     */
    @Override
    protected void onForceLoad() {
        super.onForceLoad();
    }
}

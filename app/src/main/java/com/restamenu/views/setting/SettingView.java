package com.restamenu.views.setting;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.restamenu.R;
import com.restamenu.data.preferences.KeyValueStorage;
import com.restamenu.main.CheckedItem;
import com.restamenu.model.content.Currency;
import com.restamenu.model.content.Language;
import com.restamenu.restaurant.CheckItemChangeListener;
import com.restamenu.restaurant.adapter.CheckItemAdapter;
import com.restamenu.util.Logger;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Alexandr
 */

public class SettingView extends FrameLayout implements View.OnClickListener, CheckItemChangeListener{

    private CheckItemAdapter languageAdapter;
    private CheckItemAdapter currencyAdapter;
    private KeyValueStorage keyValueStorage;

    private PopupWindow settingPopup;
    private PopupWindow currencyPopup;
    private PopupWindow languagePopup;

    private TextView curLanguageText;
    private TextView curCurrencyText;

    private TextView curCurrencyIcon;
    private ImageView curLanguageIcon;

    private Currency currentCurrency;
    private Language currentLanguge;

    private View currencyContainer;
    private View languageContainer;

    private int languageLayoutWidth = 0;
    private int currencyLayoutWidth = 0;

    private OnSettingItemChanged onSettingItemChanged;

    public SettingListener getSettingListener() {
        return settingListener;
    }

    public void setSettingListener(SettingListener settingListener) {
        this.settingListener = settingListener;
    }

    private SettingListener settingListener;

    public SettingView(@NonNull Context context) {
        super(context, null);
        initiateView();
    }

    public SettingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initiateView();
    }

    public SettingView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initStyle(attrs);
        initiateView();
    }

    public void initiateView() {
        inflate(getContext(), R.layout.restaurant_setting_view, this);

        currentCurrency = new Currency();
        currentLanguge = new Language();

        keyValueStorage = new KeyValueStorage(getContext());
        currencyAdapter = new CheckItemAdapter();
        languageAdapter = new CheckItemAdapter();

        languageAdapter.setCheckItemChangeListener(this);
        currencyAdapter.setCheckItemChangeListener(this);

        languageAdapter.setTYPE(CheckItemAdapter.LANGUAGE_TYPE);
        currencyAdapter.setTYPE(CheckItemAdapter.CURRENCY_TYPE);

        if (isTablet()) {
            languageContainer = findViewById(R.id.lang_container);
            currencyContainer = findViewById(R.id.currency_container);

            curLanguageText = findViewById(R.id.language_title);
            curCurrencyText = findViewById(R.id.currency_title);

            curCurrencyIcon = findViewById(R.id.currency_icon);
            curLanguageIcon = findViewById(R.id.country_flag);

            languageContainer.setOnClickListener(this);
            currencyContainer.setOnClickListener(this);

            initLanguagePopup();
            initCurrencyPopup();

        } else {
            View settingsContainer = findViewById(R.id.settings_container);
            settingsContainer.setOnClickListener(this);
            initSettingPopup();
        }

    }

    private void initStyle(AttributeSet attributeSet) {
        TypedArray a = getContext().obtainStyledAttributes(attributeSet, R.styleable.SettingView);
        a.recycle();
    }

    private void initLanguagePopup() {
        languagePopup = new PopupWindow(getContext());

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View layout = inflater.inflate(R.layout.setting_popup_content, null);

        TextView popupTitle = layout.findViewById(R.id.dropdown_content_title);
        RecyclerView popupList = layout.findViewById(R.id.dropdown_content_list);

        popupTitle.setText(getContext().getResources().getString(R.string.select_menu_language));

        popupList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        popupList.setAdapter(languageAdapter);

        layout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        languageContainer.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        languageLayoutWidth = (int) (layout.getMeasuredWidth() - languageContainer.getMeasuredWidth() * 1.38f);
        int height = layout.getMeasuredHeight();


        languagePopup.setOnDismissListener(()->settingListener.popupClosed());

        languagePopup.setContentView(layout);
        languagePopup.setHeight(LayoutParams.WRAP_CONTENT);
        languagePopup.setWidth(LayoutParams.WRAP_CONTENT);
        languagePopup.setOutsideTouchable(true);
        languagePopup.setFocusable(true);
        languagePopup.setBackgroundDrawable(new BitmapDrawable());

    }

    private void initCurrencyPopup() {
        currencyPopup = new PopupWindow(getContext());

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View layout = inflater.inflate(R.layout.setting_popup_content, null);

        TextView popupTitle = layout.findViewById(R.id.dropdown_content_title);
        RecyclerView popupList = layout.findViewById(R.id.dropdown_content_list);

        popupTitle.setText(getContext().getResources().getString(R.string.select_menu_currency));

        popupList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        popupList.setAdapter(currencyAdapter);


        layout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        currencyContainer.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        currencyLayoutWidth = (int) (layout.getMeasuredWidth() - currencyContainer.getMeasuredWidth() * 2.32f);
        int height = layout.getMeasuredHeight();

        currencyPopup.setOnDismissListener(()->settingListener.popupClosed());

        currencyPopup.setContentView(layout);
        currencyPopup.setHeight(LayoutParams.WRAP_CONTENT);
        currencyPopup.setWidth(LayoutParams.WRAP_CONTENT);
        currencyPopup.setOutsideTouchable(true);
        currencyPopup.setFocusable(true);
        currencyPopup.setBackgroundDrawable(new BitmapDrawable());

    }

    private void initSettingPopup() {
        settingPopup = new PopupWindow(getContext());

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View layout = inflater.inflate(R.layout.setting_popup_content, null);

        View actionCancel = layout.findViewById(R.id.action_cancel);
        actionCancel.setOnClickListener(view -> settingPopup.dismiss());

        View actionSave = layout.findViewById(R.id.button_save);
        actionSave.setOnClickListener(view -> settingPopup.dismiss());

        settingPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });

        RecyclerView currencyList = layout.findViewById(R.id.currency_list);
        RecyclerView languageList = layout.findViewById(R.id.language_list);

        currencyList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        languageList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        currencyList.setAdapter(currencyAdapter);
        languageList.setAdapter(languageAdapter);

        settingPopup.setContentView(layout);
        settingPopup.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        settingPopup.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        settingPopup.setOutsideTouchable(false);
        settingPopup.setFocusable(false);
        settingPopup.setBackgroundDrawable(new BitmapDrawable());
    }

    public void setLanguages(List<Language> languages) {
        int defaultLanguageId = keyValueStorage.getLanguageId();
        languageAdapter.setItems(new ArrayList<>());

        boolean hasDefaultLang = false;

        for (Language item: languages)
            if (item.getLanguage_id() == defaultLanguageId)
                hasDefaultLang = true;

        for (int i = 0; i < languages.size(); i++) {
            if (defaultLanguageId == languages.get(i).getLanguage_id()) {

                if (isTablet()) {
                    curLanguageText.setText(languages.get(i).getShortName());
                    curLanguageIcon.setImageResource(getLanguageFlag(languages.get(i).getLanguage_id()));
                }

                languageAdapter.addItem(new CheckedItem<>(languages.get(i), true));
                currentLanguge = languages.get(i);
            }
            else
                languageAdapter.addItem(new CheckedItem<>(languages.get(i), false));

        }

        // if user language not exist then use default language
        if (!hasDefaultLang)
            languageAdapter.checkItem(0);

    }

    public void setCurrencies(List<Currency> currencies) {
        Logger.log("Currencies from SettingView");

        int defaultCurrencyId = keyValueStorage.getCurrencyId();
        currencyAdapter.setItems(new ArrayList<>());

        boolean hasDefaultCurr = false;

        for(Currency item: currencies)
            if (item.getCurrency_id() == defaultCurrencyId)
                hasDefaultCurr = true;


        for (int i = 0; i < currencies.size(); i++) {
            if (defaultCurrencyId == currencies.get(i).getCurrency_id()) {

                if (isTablet()) {
                    if (isTablet()) {
                        curCurrencyText.setText(currencies.get(i).getShortName().toUpperCase());
                        curCurrencyIcon.setText(currencies.get(i).getSymbol());
                    }
                }

                currencyAdapter.addItem(new CheckedItem<>(currencies.get(i), true));
                currentCurrency = currencies.get(i);
            }
            else
                currencyAdapter.addItem(new CheckedItem<>(currencies.get(i), false));
        }

        // if user currency not exist then use default currency
        if (!hasDefaultCurr)
            currencyAdapter.checkItem(0);

//        Toast.makeText(getContext(), ((Currency)languageAdapter.getItem(0).getItem()).getName(), Toast.LENGTH_SHORT).show();

    }

    private void displayLanguagePopup(View anchorView) {
        if (languagePopup != null) {
            languagePopup.showAsDropDown(anchorView, -languageLayoutWidth, 0);
            settingListener.popupOpened();
        }
    }

    private void displayCurrencyPopup(View anchorView) {
        if (currencyPopup != null) {
            currencyPopup.showAsDropDown(anchorView, -currencyLayoutWidth, 0);
            settingListener.popupOpened();
        }
    }

    private void displaySettingPopup(View anchorView) {
        if (settingPopup != null) {
            settingPopup.showAtLocation(anchorView, Gravity.CENTER, 0, 0);
        }
    }

    protected boolean isTablet() {
        return getResources().getBoolean(R.bool.isLargeLayout);
    }

    public PopupWindow getSettingPopup() {
        return settingPopup;
    }

    public void dismissSettingPopup() {
        if (settingPopup != null && settingPopup.isShowing())
            settingPopup.dismiss();
    }

    public void setSettingPopupOnDismissListener(PopupWindow.OnDismissListener listener) {
        settingPopup.setOnDismissListener(listener);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.settings_container: {
                displaySettingPopup(view);
                break;
            }
            case R.id.lang_container: {
                displayLanguagePopup(view);
                break;
            }
            case R.id.currency_container: {
                displayCurrencyPopup(view);
                break;
            }
        }
    }

    @Override
    public void onCheckItemSelected(CheckedItem item) {
        if (item.getItem() instanceof Language) {
            Language language = ((Language) item.getItem());

            keyValueStorage.setLanguageId((language.getLanguage_id()));

            if (isTablet()) {
                curLanguageText.setText(language.getShortName());
                curLanguageIcon.setImageResource(getLanguageFlag(language.getLanguage_id()));
            }

            currentLanguge = language;
            onSettingItemChanged.onLanguageChanged(language);

        } else if (item.getItem() instanceof Currency) {
            Currency currency = ((Currency) item.getItem());

            keyValueStorage.setCurrencyId(currency.getCurrency_id());

            if (isTablet()) {
                curCurrencyText.setText(currency.getShortName().toUpperCase());
                curCurrencyIcon.setText(currency.getSymbol());
            }

            currentCurrency = currency;
            onSettingItemChanged.onCurrencyChanged(currency);
        }

    }

    public int getLanguageFlag(int langId) {
        switch (langId) {
            case 1: {
                return R.drawable.ic_flag_uk;
            }
            case 2: {
                return R.drawable.ic__flag_russia;
            }
            case 3: {
                return R.drawable.ic_flag_arabic;
            }
        }
        return -1;
    }


    public OnSettingItemChanged getOnSettingItemChanged() {
        return onSettingItemChanged;
    }

    public void setOnSettingItemChanged(OnSettingItemChanged onSettingItemChanged) {
        this.onSettingItemChanged = onSettingItemChanged;
    }

    public Currency getCurrentCurrency() {
        return currentCurrency;
    }

    public void setCurrentCurrency(Currency currentCurrency) {
        this.currentCurrency = currentCurrency;
    }

    public Language getCurrentLanguge() {
        return currentLanguge;
    }

    public void setCurrentLanguge(Language currentLanguge) {
        this.currentLanguge = currentLanguge;
    }
}

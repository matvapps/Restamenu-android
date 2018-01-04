package com.restamenu.views.search;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.restamenu.R;
import com.restamenu.main.PopupDropDownAdapter;
import com.restamenu.main.PopupFilterItem;
import com.restamenu.model.content.Cusine;
import com.restamenu.model.content.Institute;
import com.restamenu.model.content.Restaurant;
import com.restamenu.util.Logger;
import com.restamenu.views.custom.ExpandedListView;
import com.restamenu.views.search.utils.AnimationUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Roodie
 */

public class RestaurantsSearchView extends FrameLayout implements Filter.FilterListener, PopupDropDownAdapter.FilterItemChangeListener {
    public static final int REQUEST_VOICE = 9999;
    private SearchListener searchListener;
    private MenuItem mMenuItem;
    private boolean mIsSearchOpen = false;
    private int mAnimationDuration;
    private boolean mClearingFocus;
    //Views
    private View mSearchLayout;
    private View mTintView;
    private ExpandedListView mSuggestionsListView;
    private EditText mSearchSrcTextView;
    private LinearLayout selectInstituteView;
    private LinearLayout selectCuisineView;
    private View selectFilterView;
    private LinearLayout searchBtn;
    //private ImageButton mBackBtn;
    //private ImageButton mVoiceBtn;
    private ImageButton mEmptyBtn;
    private LinearLayout mSearchTopBar;
    private CharSequence mOldQueryText;
    private CharSequence mUserQuery;
    private OnQueryTextListener mOnQueryChangeListener;
    private SearchViewListener mSearchViewListener;
    private PopupDropDownAdapter cuisinePopupDropdownAdapter;
    private PopupDropDownAdapter institutePopupDropdownAdapter;
    private PopupWindow cuisinePopup;
    private PopupWindow institutePopup;
    private PopupWindow filterPopup;
    private ListAdapter mAdapter;
    private List<PopupFilterItem> filterList;
    private final OnClickListener mOnClickListener = new OnClickListener() {

        public void onClick(View v) {
            if (v == selectCuisineView) {
                displayCuisinePopupWindow(v);
            } else if (v == selectInstituteView) {
                displayInstitutePopupWindow(v);
            } else if (v == searchBtn) {
                onSubmitQuery();
            } else if (v == mEmptyBtn) {
                mSearchSrcTextView.setText(null);
            } else if (v == mSearchSrcTextView) {
                showSuggestions();
            } else if (v == mTintView) {
                closeSearch();
            } else if (v == selectFilterView) {
                Logger.log("Select filter view");
                displayFilterPopupWindow(v);
            }
        }
    };
    private SavedState mSavedState;
    private boolean submit = false;

    private boolean ellipsize = false;

    private boolean allowVoiceSearch;
    private Drawable suggestionIcon;

    private Context mContext;

    public RestaurantsSearchView(Context context) {
        this(context, null);
    }

    public RestaurantsSearchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RestaurantsSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs);

        mContext = context;

        initiateView();

        initStyle(attrs, defStyleAttr);
    }

    private void initStyle(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.RestaurantsSearchView, defStyleAttr, 0);

        if (a != null) {
            if (a.hasValue(R.styleable.RestaurantsSearchView_searchBackground)) {
                setBackground(a.getDrawable(R.styleable.RestaurantsSearchView_searchBackground));
            }

            if (a.hasValue(R.styleable.RestaurantsSearchView_android_textColor)) {
                setTextColor(a.getColor(R.styleable.RestaurantsSearchView_android_textColor, 0));
            }

            if (a.hasValue(R.styleable.RestaurantsSearchView_android_textColorHint)) {
                setHintTextColor(a.getColor(R.styleable.RestaurantsSearchView_android_textColorHint, 0));
            }

            if (a.hasValue(R.styleable.RestaurantsSearchView_android_hint)) {
                setHint(a.getString(R.styleable.RestaurantsSearchView_android_hint));
            }

            if (a.hasValue(R.styleable.RestaurantsSearchView_searchCloseIcon)) {
                setCloseIcon(a.getDrawable(R.styleable.RestaurantsSearchView_searchCloseIcon));
            }

            if (a.hasValue(R.styleable.RestaurantsSearchView_searchSuggestionBackground)) {
                setSuggestionBackground(a.getDrawable(R.styleable.RestaurantsSearchView_searchSuggestionBackground));
            }

            if (a.hasValue(R.styleable.RestaurantsSearchView_searchSuggestionIcon)) {
                setSuggestionIcon(a.getDrawable(R.styleable.RestaurantsSearchView_searchSuggestionIcon));
            }

            if (a.hasValue(R.styleable.RestaurantsSearchView_android_inputType)) {
                setInputType(a.getInt(R.styleable.RestaurantsSearchView_android_inputType, EditorInfo.TYPE_NULL));
            }

            a.recycle();
        }
    }

    private void initiateView() {
        LayoutInflater.from(mContext).inflate(R.layout.restaurant_search_view, this, true);
        mSearchLayout = findViewById(R.id.search_layout);

        mSearchTopBar = (LinearLayout) mSearchLayout.findViewById(R.id.search_top_bar);
        mSuggestionsListView = (ExpandedListView) mSearchLayout.findViewById(R.id.suggestion_list);
        mSearchSrcTextView = (EditText) mSearchLayout.findViewById(R.id.searchTextView);
        selectCuisineView = (LinearLayout) mSearchLayout.findViewById(R.id.action_choose_cuisine);
        selectInstituteView = (LinearLayout) mSearchLayout.findViewById(R.id.action_choose_institute);
        selectFilterView = mSearchLayout.findViewById(R.id.action_choose_filter);
        searchBtn = (LinearLayout) mSearchLayout.findViewById(R.id.action_search);
        mEmptyBtn = (ImageButton) mSearchLayout.findViewById(R.id.action_empty_btn);
        mTintView = mSearchLayout.findViewById(R.id.transparent_view);


        allowVoiceSearch = false;

        if (!isTablet()) {
            filterPopup = new PopupWindow(getContext());
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View layout = inflater.inflate(R.layout.filter_popup_content, null);
            View actionCancel = layout.findViewById(R.id.action_cancel);

            actionCancel.setOnClickListener(view -> filterPopup.dismiss());

            filterPopup.setContentView(layout);
            filterPopup.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
            filterPopup.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
            filterPopup.setOutsideTouchable(false);
            filterPopup.setFocusable(false);
            filterPopup.setBackgroundDrawable(new BitmapDrawable());
        }

        initSearchView();

        if (isTablet()) {
            selectInstituteView.setOnClickListener(mOnClickListener);
            selectCuisineView.setOnClickListener(mOnClickListener);
        } else {
            selectFilterView.setOnClickListener(mOnClickListener);
        }

        mSearchSrcTextView.setOnClickListener(mOnClickListener);
        searchBtn.setOnClickListener(mOnClickListener);

        mEmptyBtn.setOnClickListener(mOnClickListener);
        mTintView.setOnClickListener(mOnClickListener);

        mSuggestionsListView.setVisibility(GONE);
        setAnimationDuration(AnimationUtil.ANIMATION_DURATION_MEDIUM);
    }

    private void initSearchView() {

        cuisinePopupDropdownAdapter = new PopupDropDownAdapter();
        institutePopupDropdownAdapter = new PopupDropDownAdapter();

        filterList = new ArrayList<>();

        mSearchSrcTextView.setOnEditorActionListener((v, actionId, event) -> {
            //Removed only on pressing on button
            //onSubmitQuery();
            return true;
        });

        mSearchSrcTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mUserQuery = s;
                startFilter(s);
                RestaurantsSearchView.this.onTextChanged(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mSearchSrcTextView.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showKeyboard(mSearchSrcTextView);
                    showSuggestions();
                }
            }
        });
    }

    private void startFilter(CharSequence s) {
        if (mAdapter != null && mAdapter instanceof Filterable) {
            ((Filterable) mAdapter).getFilter().filter(s, RestaurantsSearchView.this);
        }
    }

    public void setSearchListener(SearchListener searchListener) {
        this.searchListener = searchListener;

        cuisinePopupDropdownAdapter.setFilterItemChangeListener(this);
        institutePopupDropdownAdapter.setFilterItemChangeListener(this);
    }

    private void displayFilterPopupWindow(View anchorView) {
        if (filterPopup != null) {
            filterPopup.showAtLocation(anchorView, Gravity.CENTER, 0, 0);
        }
    }

    private void displayCuisinePopupWindow(View anchorView) {
        if (cuisinePopup != null) {
            cuisinePopup.showAsDropDown(anchorView);
        }
    }

    private void displayInstitutePopupWindow(View anchorView) {
        if (institutePopup != null)
            institutePopup.showAsDropDown(anchorView);
    }

    public void setCuisines(List<Cusine> cusines) {

        for (int i = 0; i < cusines.size(); i++) {
            cuisinePopupDropdownAdapter.addItem(new PopupFilterItem<>(cusines.get(i), false));
        }

        if (isTablet()) {
            cuisinePopup = new PopupWindow(getContext());
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View layout = inflater.inflate(R.layout.popup_content, null);

            TextView textView = layout.findViewById(R.id.dropdown_content_title);
            RecyclerView recyclerView = layout.findViewById(R.id.dropdown_content_grid);

            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
            recyclerView.setAdapter(cuisinePopupDropdownAdapter);

            String titlePattern = getResources().getString(R.string.popup_dropdown_title);
            textView.setText(String.format(titlePattern, "cuisine"));

            cuisinePopup.setContentView(layout);
            cuisinePopup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
            cuisinePopup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
            cuisinePopup.setOutsideTouchable(true);
            cuisinePopup.setFocusable(true);
            cuisinePopup.setBackgroundDrawable(new BitmapDrawable());
        } else {
            View layout = filterPopup.getContentView();
            TextView cuisineTitle = layout.findViewById(R.id.filter_cuisine_title);
            RecyclerView cuisineList = layout.findViewById(R.id.filter_cuisine_list);

            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                    LinearLayoutManager.VERTICAL, false);

            cuisineList.setFocusable(false);
            layout.findViewById(R.id.filter_text).requestFocus();

            String titlePattern = getResources().getString(R.string.popup_dropdown_title);
            cuisineTitle.setText(String.format(titlePattern, "cuisine"));

            cuisineList.setNestedScrollingEnabled(false);
            cuisineList.setLayoutManager(layoutManager);
            cuisineList.setAdapter(cuisinePopupDropdownAdapter);

        }
    }

    public void setInstitutions(List<Institute> data) {
        for (int i = 0; i < data.size(); i++) {
            institutePopupDropdownAdapter.addItem(new PopupFilterItem<>(data.get(i), false));
        }

        if (isTablet()) {
            institutePopup = new PopupWindow(getContext());
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View layout = inflater.inflate(R.layout.popup_content, null);

            TextView textView = layout.findViewById(R.id.dropdown_content_title);
            RecyclerView recyclerView = layout.findViewById(R.id.dropdown_content_grid);

            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
            recyclerView.setAdapter(institutePopupDropdownAdapter);

            String titlePattern = getResources().getString(R.string.popup_dropdown_title);
            textView.setText(String.format(titlePattern, "institution"));

            institutePopup.setContentView(layout);
            institutePopup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
            institutePopup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
            institutePopup.setOutsideTouchable(true);
            institutePopup.setFocusable(true);
            institutePopup.setBackgroundDrawable(new BitmapDrawable());
        } else {
            View layout = filterPopup.getContentView();
            TextView instituteTitle = layout.findViewById(R.id.filter_institute_title);
            RecyclerView instituteList = layout.findViewById(R.id.filter_institute_list);

            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                    LinearLayoutManager.VERTICAL, false);

            instituteList.setFocusable(false);
            layout.findViewById(R.id.filter_text).requestFocus();

            String titlePattern = getResources().getString(R.string.popup_dropdown_title);
            instituteTitle.setText(String.format(titlePattern, "institution"));

            instituteList.setNestedScrollingEnabled(false);
            instituteList.setLayoutManager(layoutManager);
            instituteList.setAdapter(institutePopupDropdownAdapter);
        }
    }

    private void onTextChanged(CharSequence newText) {
        CharSequence text = mSearchSrcTextView.getText();
        mUserQuery = text;
        boolean hasText = !TextUtils.isEmpty(text);
        if (hasText) {
            mEmptyBtn.setVisibility(VISIBLE);
        } else {
            mEmptyBtn.setVisibility(GONE);
        }

        if (mOnQueryChangeListener != null && !TextUtils.equals(newText, mOldQueryText)) {
            mOnQueryChangeListener.onQueryTextChange(newText.toString());
        }
        mOldQueryText = newText.toString();
    }

    private void onSubmitQuery() {
        CharSequence query = mSearchSrcTextView.getText();
        if (query != null && TextUtils.getTrimmedLength(query) > 0) {
            if (mOnQueryChangeListener == null || !mOnQueryChangeListener.onQueryTextSubmit(query.toString())) {
//                closeSearch();
                mSearchSrcTextView.setText(null);
            }
            searchListener.onPerformSearch(query);
        }
    }

    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void showKeyboard(View view) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1 && view.hasFocus()) {
            view.clearFocus();
        }
        view.requestFocus();
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, 0);
    }

    @Override
    public void setBackground(Drawable background) {
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mSearchTopBar.setBackground(background);
        } else {
            mSearchTopBar.setBackgroundDrawable(background);
        }*/
    }

    //Public Attributes

    @Override
    public void setBackgroundColor(int color) {
        mSearchTopBar.setBackgroundColor(color);
    }

    public void setTextColor(int color) {
        mSearchSrcTextView.setTextColor(color);
    }

    public void setHintTextColor(int color) {
        mSearchSrcTextView.setHintTextColor(color);
    }

    public void setHint(CharSequence hint) {
        mSearchSrcTextView.setHint(hint);
    }

    public void setCloseIcon(Drawable drawable) {
        mEmptyBtn.setImageDrawable(drawable);
    }

    public void setSuggestionIcon(Drawable drawable) {
        suggestionIcon = drawable;
    }

    public void setInputType(int inputType) {
        mSearchSrcTextView.setInputType(inputType);
    }

    public void setSuggestionBackground(Drawable background) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mSuggestionsListView.setBackground(background);
        } else {
            mSuggestionsListView.setBackgroundDrawable(background);
        }
    }

    public void setCursorDrawable(int drawable) {
        try {
            // https://github.com/android/platform_frameworks_base/blob/kitkat-release/core/java/android/widget/TextView.java#L562-564
            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(mSearchSrcTextView, drawable);
        } catch (Exception ignored) {
            Log.e("MaterialSearchView", ignored.toString());
        }
    }

    public void setVoiceSearch(boolean voiceSearch) {
        allowVoiceSearch = voiceSearch;
    }

    /**
     * Call this method to show suggestions list. This shows up when adapter is set. Call {@link #setAdapter(ListAdapter)} before calling this.
     */
    public void showSuggestions() {
        if (mAdapter != null && mAdapter.getCount() > 0 && mSuggestionsListView.getVisibility() == GONE) {
            mSuggestionsListView.setVisibility(VISIBLE);
        }
    }

    //Public Methods

    /**
     * Submit the query as soon as the user clicks the item.
     *
     * @param submit submit state
     */
    public void setSubmitOnClick(boolean submit) {
        this.submit = submit;
    }

    /**
     * Set Suggest List OnItemClickListener
     *
     * @param listener
     */
    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        mSuggestionsListView.setOnItemClickListener(listener);
    }

    /**
     * Set Adapter for suggestions list. Should implement Filterable.
     *
     * @param adapter
     */
    public void setAdapter(ListAdapter adapter) {
        mAdapter = adapter;
        mSuggestionsListView.setAdapter(adapter);
        startFilter(mSearchSrcTextView.getText());
    }

    /**
     * Set Adapter for suggestions list with the given suggestion array
     *
     * @param suggestions array of suggestions
     */
    public void setSuggestions(ArrayList<String> suggestions) {
        if (suggestions != null && suggestions.size() > 0) {
            mTintView.setVisibility(VISIBLE);
            final SearchAdapter adapter = new SearchAdapter(mContext, suggestions, suggestionIcon, ellipsize);
            setAdapter(adapter);

            setOnItemClickListener((parent, view, position, id) -> setQuery((String) adapter.getItem(position), submit));
        } else {
            mTintView.setVisibility(GONE);
        }
    }

    /**
     * Set Adapter for suggestions list with the given suggestion list
     *
     * @param suggestions list of suggested restaurants
     */
    public void setSuggestions(List<Restaurant> suggestions) {

        //TODO
    }

    /**
     * Dismiss the suggestions list.
     */
    public void dismissSuggestions() {
        if (mSuggestionsListView.getVisibility() == VISIBLE) {
            mSuggestionsListView.setVisibility(GONE);
        }
    }

    /**
     * Calling this will set the query to search text box. if submit is true, it'll submit the query.
     *
     * @param query
     * @param submit
     */
    public void setQuery(CharSequence query, boolean submit) {
        mSearchSrcTextView.setText(query);
        if (query != null) {
            mSearchSrcTextView.setSelection(mSearchSrcTextView.length());
            mUserQuery = query;
        }
        if (submit && !TextUtils.isEmpty(query)) {
            onSubmitQuery();
        }
    }

    /**
     * Call this method and pass the menu item so this class can handle click events for the Menu Item.
     *
     * @param menuItem
     */
    public void setMenuItem(MenuItem menuItem) {
        this.mMenuItem = menuItem;
        mMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                showSearch();
                return true;
            }
        });
    }

    /**
     * Return true if search is open
     *
     * @return
     */
    public boolean isSearchOpen() {
        return mIsSearchOpen;
    }

    /**
     * Sets animation duration. ONLY FOR PRE-LOLLIPOP!!
     *
     * @param duration duration of the animation
     */
    public void setAnimationDuration(int duration) {
        mAnimationDuration = duration;
    }

    /**
     * Open Search View. This will animate the showing of the view.
     */
    public void showSearch() {
        showSearch(true);
    }

    /**
     * Open Search View. If animate is true, Animate the showing of the view.
     *
     * @param animate true for animate
     */
    public void showSearch(boolean animate) {
        if (isSearchOpen()) {
            return;
        }

        //Request Focus
        mSearchSrcTextView.setText(null);
        mSearchSrcTextView.requestFocus();

        if (animate) {
            setVisibleWithAnimation();

        } else {
            mSearchLayout.setVisibility(VISIBLE);
            if (mSearchViewListener != null) {
                mSearchViewListener.onSearchViewShown();
            }
        }
        mIsSearchOpen = true;
    }

    private void setVisibleWithAnimation() {
        AnimationUtil.AnimationListener animationListener = new AnimationUtil.AnimationListener() {
            @Override
            public boolean onAnimationStart(View view) {
                return false;
            }

            @Override
            public boolean onAnimationEnd(View view) {
                if (mSearchViewListener != null) {
                    mSearchViewListener.onSearchViewShown();
                }
                return false;
            }

            @Override
            public boolean onAnimationCancel(View view) {
                return false;
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mSearchLayout.setVisibility(View.VISIBLE);
            AnimationUtil.reveal(mSearchTopBar, animationListener);

        } else {
            AnimationUtil.fadeInView(mSearchLayout, mAnimationDuration, animationListener);
        }
    }

    /**
     * Close search view.
     */
    public void closeSearch() {
        if (!isSearchOpen()) {
            return;
        }

        mSearchSrcTextView.setText(null);
        dismissSuggestions();
        clearFocus();

        mSearchLayout.setVisibility(GONE);
        if (mSearchViewListener != null) {
            mSearchViewListener.onSearchViewClosed();
        }
        mIsSearchOpen = false;

    }

    /**
     * Set this listener to listen to Query Change events.
     *
     * @param listener
     */
    public void setOnQueryTextListener(OnQueryTextListener listener) {
        mOnQueryChangeListener = listener;
    }

    /**
     * Set this listener to listen to Search View open and close events
     *
     * @param listener
     */
    public void setOnSearchViewListener(SearchViewListener listener) {
        mSearchViewListener = listener;
    }

    /**
     * Ellipsize suggestions longer than one line.
     *
     * @param ellipsize
     */
    public void setEllipsize(boolean ellipsize) {
        this.ellipsize = ellipsize;
    }

    @Override
    public void onFilterComplete(int count) {
        if (count > 0) {
            showSuggestions();
        } else {
            dismissSuggestions();
        }
    }

    @Override
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        // Don't accept focus if in the middle of clearing focus
        if (mClearingFocus) return false;
        // Check if SearchView is focusable.
        if (!isFocusable()) return false;
        return mSearchSrcTextView.requestFocus(direction, previouslyFocusedRect);
    }

    @Override
    public void clearFocus() {
        mClearingFocus = true;
        hideKeyboard(this);
        super.clearFocus();
        mSearchSrcTextView.clearFocus();
        mClearingFocus = false;
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        mSavedState = new SavedState(superState);
        mSavedState.query = mUserQuery != null ? mUserQuery.toString() : null;
        mSavedState.isSearchOpen = this.mIsSearchOpen;

        return mSavedState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        mSavedState = (SavedState) state;

        if (mSavedState.isSearchOpen) {
            showSearch(false);
            setQuery(mSavedState.query, false);
        }

        super.onRestoreInstanceState(mSavedState.getSuperState());
    }

    @Override
    public void onFilterItemChanged(PopupFilterItem item, boolean isEnable) {
        if (item.getItem() instanceof Cusine) {
            if (!item.isChecked()) {
                removeFromFilterList(item);
            } else {
                addToFilterList(item);
            }
        }
    }

    public List<PopupFilterItem> getFilterList() {
        return filterList;
    }

    public void addToFilterList(PopupFilterItem item) {
        filterList.add(item);
    }

    public void removeFromFilterList(PopupFilterItem item) {
        filterList.remove(item);
    }

    public void setFilterList(List<PopupFilterItem> filterList) {
        this.filterList = filterList;
    }


    public interface SearchListener {
        void onPerformSearch(CharSequence searchString);

        void onInstituteChanged(PopupFilterItem item);

        void onCuisineChanged(PopupFilterItem item);
    }

    public interface OnQueryTextListener {

        /**
         * Called when the user submits the query. This could be due to a key press on the
         * keyboard or due to pressing a submit button.
         * The listener can override the standard behavior by returning true
         * to indicate that it has handled the submit request. Otherwise return false to
         * let the SearchView handle the submission by launching any associated intent.
         *
         * @param query the query text that is to be submitted
         * @return true if the query has been handled by the listener, false to let the
         * SearchView perform the default action.
         */
        boolean onQueryTextSubmit(String query);

        /**
         * Called when the query text is changed by the user.
         *
         * @param newText the new content of the query text field.
         * @return false if the SearchView should perform the default action of showing any
         * suggestions if available, true if the action was handled by the listener.
         */
        boolean onQueryTextChange(String newText);
    }

    public interface SearchViewListener {
        void onSearchViewShown();

        void onSearchViewClosed();
    }

    static class SavedState extends BaseSavedState {
        //required field that makes Parcelables from a Parcel
        public static final Creator<SavedState> CREATOR =
                new Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
        String query;
        boolean isSearchOpen;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.query = in.readString();
            this.isSearchOpen = in.readInt() == 1;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeString(query);
            out.writeInt(isSearchOpen ? 1 : 0);
        }
    }

    protected boolean isTablet() {
        return getResources().getBoolean(R.bool.isLargeLayout);
    }

}

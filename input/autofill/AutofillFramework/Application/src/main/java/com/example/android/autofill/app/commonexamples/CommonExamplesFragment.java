/*
* Copyright (C) 2017 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.example.android.autofill.app.commonexamples;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.autofill.app.BaseMainFragment;
import com.example.android.autofill.app.R;
import com.example.android.autofill.app.serviceverification.CreditCardActivity;
import com.example.android.autofill.app.serviceverification.CreditCardAntiPatternActivity;
import com.example.android.autofill.app.serviceverification.MultiplePartitionsActivity;
import com.example.android.autofill.app.view.widget.NavigationItem;

public class CommonExamplesFragment extends BaseMainFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_common_examples, container, false);
        NavigationItem loginEditTexts = root.findViewById(R.id.standardViewSignInButton);
        NavigationItem loginCustomVirtual = root.findViewById(R.id.virtualViewSignInButton);
        NavigationItem creditCard = root.findViewById(R.id.creditCardButton);
        NavigationItem creditCardSpinners = root.findViewById(R.id.creditCardSpinnersButton);
        NavigationItem loginAutoComplete = root.findViewById(R.id.standardLoginWithAutoCompleteButton);
        NavigationItem emailCompose = root.findViewById(R.id.emailComposeButton);
        NavigationItem creditCardCompoundView = root.findViewById(R.id.creditCardCompoundViewButton);
        NavigationItem creditCardDatePicker = root.findViewById(R.id.creditCardDatePickerButton);
        NavigationItem creditCardAntiPatternPicker = root.findViewById(R.id.creditCardAntiPatternButton);
        NavigationItem multiplePartitions = root.findViewById(R.id.multiplePartitionsButton);
        NavigationItem loginWebView = root.findViewById(R.id.webviewSignInButton);
        loginEditTexts.setNavigationButtonClickListener((view) ->
                startActivity(StandardSignInActivity.getStartActivityIntent(getContext()))
        );
        loginCustomVirtual.setNavigationButtonClickListener((view) ->
                startActivity(VirtualSignInActivity.getStartActivityIntent(getContext()))
        );
        creditCard.setNavigationButtonClickListener((view) ->
                startActivity(CreditCardActivity.getStartActivityIntent(getContext()))
        );
        creditCardSpinners.setNavigationButtonClickListener((view) ->
                startActivity(CreditCardSpinnersActivity.getStartActivityIntent(getContext()))
        );
        loginAutoComplete.setNavigationButtonClickListener((view) ->
                startActivity(
                        StandardAutoCompleteSignInActivity.getStartActivityIntent(getContext()))
        );
        emailCompose.setNavigationButtonClickListener((view) ->
                startActivity(EmailComposeActivity.getStartActivityIntent(getContext()))
        );
        creditCardCompoundView.setNavigationButtonClickListener((view) ->
                startActivity(CreditCardCompoundViewActivity.getStartActivityIntent(getContext()))
        );
        creditCardDatePicker.setNavigationButtonClickListener((view) ->
                startActivity(CreditCardDatePickerActivity.getStartActivityIntent(getContext()))
        );
        creditCardAntiPatternPicker.setNavigationButtonClickListener((view) ->
                startActivity(CreditCardAntiPatternActivity.getStartActivityIntent(getContext()))
        );
        multiplePartitions.setNavigationButtonClickListener((view) ->
                startActivity(MultiplePartitionsActivity.getStartActivityIntent(getContext()))
        );
        loginWebView.setNavigationButtonClickListener((view) ->
                startActivity(WebViewSignInActivity.getStartActivityIntent(getContext()))
        );
        return root;
    }

    @Override
    public int getPageTitleResId() {
        return R.string.common_examples_page_title;
    }
}

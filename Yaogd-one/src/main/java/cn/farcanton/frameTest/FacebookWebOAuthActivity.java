/*
 * Copyright 2011 the original author or authors.
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
package cn.farcanton.frameTest;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * @author Roy Clarkson
 */
public class FacebookWebOAuthActivity extends AbstractWebViewActivity {

    private static final String TAG = FacebookWebOAuthActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Facebook uses javascript to redirect to the success page
        getWebView().getSettings().setJavaScriptEnabled(true);

        // Using a custom web view client to capture the access token
        getWebView().setWebViewClient(new FacebookOAuthWebViewClient());

    }

    @Override
    public void onStart() {
        super.onStart();

        // display the Facebook authorization page
        getWebView().loadUrl("http://www.baidu.com");
    }

    private class FacebookOAuthWebViewClient extends WebViewClient {

        /*
         * The WebViewClient has another method called shouldOverridUrlLoading
         * which does not capture the javascript redirect to the success page.
         * So we're using onPageStarted to capture the url.
         */
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // parse the captured url
            Uri uri = Uri.parse(url);

            Log.d(TAG, url);

            /*
             * The access token is returned in the URI fragment of the URL See
             * the Desktop Apps section all the way at the bottom of this link:
             * http://developers.facebook.com/docs/authentication/
             * 
             * The fragment will be formatted like this:
             * 
             * #access_token=A&expires_in=0
             */
            String uriFragment = uri.getFragment();

            // confirm we have the fragment, and it has an access_token
            // parameter
            if (uriFragment != null && uriFragment.startsWith("access_token=")) {

                /*
                 * The fragment also contains an "expires_in" parameter. In this
                 * example we requested the offline_access permission, which
                 * basically means the access will not expire, so we're ignoring
                 * it here
                 */
                try {
                    // split to get the two different parameters
                    String[] params = uriFragment.split("&");

                    // split to get the access token parameter and value
                    String[] accessTokenParam = params[0].split("=");

                    // get the access token value
                    String accessToken = accessTokenParam[1];
                } catch (Exception e) {
                    // don't do anything if the parameters are not what is
                    // expected
                }

            }

        }
    }
}

/*
 * Copyright 2020 Kato Shinya.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.thinkit.api.currencyexchange;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import org.thinkit.api.common.Communicable;

/**
 * 為替レートAPIへリクエストを送信する処理を定義したクラスです。為替レートAPIは欧州中央銀行のWeb APIを使用します。
 *
 * @author Kato Shinya
 * @since 1.0
 * @version 1.0
 */
public final class CurrencyExchangeRates implements Communicable {

    /**
     * API呼び出し時のURL
     */
    private static final String EXCHANGE_RATES_API = "https://api.exchangeratesapi.io/latest";

    @Override
    public HttpResponse<String> send() {
        final HttpRequest request = HttpRequest.newBuilder().uri(URI.create(EXCHANGE_RATES_API))
                .headers("Authorization", "Bearer aabbcc112233", "Accept", "application/json").GET().build();

        try {
            return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }
}
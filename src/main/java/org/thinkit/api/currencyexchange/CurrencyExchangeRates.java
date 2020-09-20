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
import java.util.ArrayList;
import java.util.List;

import org.thinkit.api.common.Communicable;
import org.thinkit.api.common.catalog.ContentType;
import org.thinkit.api.common.exception.ApiRequestFailedException;
import org.thinkit.api.currencyexchange.catalog.Currency;

import lombok.NonNull;

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

    /**
     * 基軸通貨
     */
    private Currency base;

    /**
     * シンボルリスト
     */
    private List<Currency> symbols;

    /**
     * 検索開始日
     */
    private String startAt = "";

    /**
     * 検索終了日
     */
    private String endAt = "";

    /**
     * デフォルトコンストラクタ
     */
    private CurrencyExchangeRates() {
    }

    /**
     * {CurrencyExchangeRates} クラスのインスタンスを生成するビルダークラスを返却します。
     *
     * @return {CurrencyExchangeRates} クラスのインスタンスを生成するビルダークラス
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * {@link CurrencyExchangeRates} クラスのインスタンスを生成するビルダークラスです。
     *
     * @author Kato Shinya
     * @since 1.0
     * @version 1.0
     */
    public static class Builder {

        /**
         * 基軸通貨
         */
        private Currency base = Currency.USA_DOLLAR;

        /**
         * シンボルリスト
         */
        private List<Currency> symbols = new ArrayList<>(0);

        /**
         * 検索開始日
         */
        private String startAt = "";

        /**
         * 検索終了日
         */
        private String endAt = "";

        /**
         * デフォルトコンストラクタ
         */
        private Builder() {
        }

        public Builder withBaseCurrency(@NonNull Currency base) {
            this.base = base;
            return this;
        }

        public Builder withSymbolCurrencies(@NonNull List<Currency> symbols) {
            this.symbols = symbols;
            return this;
        }

        public Builder withStartDateAt(@NonNull String startAt) {
            this.startAt = startAt;
            return this;
        }

        public Builder withEndDateAt(@NonNull String endAt) {
            this.endAt = endAt;
            return this;
        }

        public Communicable build() {

            final CurrencyExchangeRates api = new CurrencyExchangeRates();
            api.base = this.base;
            api.symbols = this.symbols;
            api.startAt = this.startAt;
            api.endAt = this.endAt;

            return api;
        }
    }

    @Override
    public HttpResponse<String> send() {

        final HttpRequest request = HttpRequest.newBuilder().uri(URI.create(EXCHANGE_RATES_API))
                .headers(ContentType.JSON.getTag()).GET().build();

        try {
            return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        } catch (IOException | InterruptedException e) {
            throw new ApiRequestFailedException(e);
        }
    }
}
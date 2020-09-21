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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.thinkit.api.common.Communicable;
import org.thinkit.api.common.catalog.ContentType;
import org.thinkit.api.common.exception.ApiRequestFailedException;
import org.thinkit.api.currencyexchange.catalog.Currency;

import lombok.NonNull;

/**
 * 為替レートAPIへリクエストを送信する処理を定義したクラスです。為替レートの取得は欧州中央銀行のWeb APIを使用します。
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
     *
     * @see #withBaseCurrency(Currency)
     * @see #withSymbolCurrencies(List)
     * @see #withStartDateAt(String)
     * @see #withEndDateAt(String)
     * @see #build()
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

        /**
         * 為替レート取得時の基軸通貨を設定します。
         * <p>
         * 初期値として {@link Currency#USA_DOLLAR} が設定されているため、USA Dollar
         * を基軸通貨にする場合は当メソッドを呼び出す必要はありません。
         *
         * @param base 基軸通貨
         * @return 自分自身のインスタンス
         *
         * @exception NullPointerException 引数として {@code null} が渡されたば場合
         */
        public Builder withBaseCurrency(@NonNull Currency base) {
            this.base = base;
            return this;
        }

        /**
         * 取得する為替レート種別を設定します。渡された種別リストが空リストの場合は無視されます。
         * <p>
         * 全種別の為替レート種別を取得する場合は当メソッドを呼び出す必要はありません。
         *
         * @param symbols 取得する為替レート種別のリスト
         * @return 自分自身のインスタンス
         *
         * @exception NullPointerException 引数として {@code null} が渡された場合
         */
        public Builder withSymbolCurrencies(@NonNull List<Currency> symbols) {
            this.symbols = symbols;
            return this;
        }

        /**
         * 為替レートを取得する際の開始日を設定します。
         * <p>
         * 当メソッドの呼び出しは任意ですが、呼び出した際には {@link #withEndDateAt(String)}
         * メソッドの呼び出しを確認してください。開始日が設定された状態で {@link #withEndDateAt(String)}
         * メソッドが呼び出されず終了日が未設定の場合は開始日から {@link CurrencyExchangeRates}
         * メソッドを呼び出した当日までの為替レートを取得します。
         *
         * @param startAt 開始日
         * @return 自分自身のインスタンス
         *
         * @exception NullPointerException 引数として {@code null} が渡された場合
         * @see #withEndDateAt(String)
         */
        public Builder withStartDateAt(@NonNull String startAt) {
            this.startAt = startAt;
            return this;
        }

        /**
         * 為替レートを取得する際の終了日を設定します。
         * <p>
         * 当メソッドの呼び出しは任意ですが、呼び出した際には {@link #withStartDateAt(String)}
         * メソッドの呼び出しを確認してください。終了日が設定された状態で {@link #withStartDateAt(String)}
         * メソッドが呼び出されず開始日が未設定の場合は終了日から半年以内の為替レートを取得します。
         *
         * @param endAt 終了日
         * @return 自分自身のインスタンス
         *
         * @exception NullPointerException 引数として {@code null} が渡された場合
         * @see #withStartDateAt(String)
         */
        public Builder withEndDateAt(@NonNull String endAt) {
            this.endAt = endAt;
            return this;
        }

        /**
         * 設定された値を基に {@link CurrencyExchangeRates} クラスの新しいインスタンスを生成し返却します。
         * <p>
         * リクエストパラメータの設定が必要ない場合は {@link Builder} クラスのインスタンスを生成し直接この
         * {@link Builder#build()} メソッドを実行しても問題ありません。
         *
         * @return {@link CurrencyExchangeRates} クラスの新しいインスタンス
         */
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

        final Map<String> requestParameters = new HashMap<>();

        final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        String.format("%s?%s", EXCHANGE_RATES_API, super.createRequestParameter(requestParameters))))
                .headers(ContentType.JSON.getTag()).GET().build();

        try {
            return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        } catch (IOException | InterruptedException e) {
            throw new ApiRequestFailedException(e);
        }
    }
}
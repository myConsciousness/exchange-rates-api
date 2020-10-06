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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.thinkit.api.common.Communicable;
import org.thinkit.api.common.Resource;
import org.thinkit.api.common.entity.RequestParameter;
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
    private static final String EXCHANGE_RATES_API = "https://api.exchangeratesapi.io";

    /**
     * 為替レートAPIのリソース
     */
    private Resource resource;

    /**
     * リクエストパラメーター
     */
    private RequestParameter requestParameter;

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
         * メソッドの呼び出しを行い終了日を設定してください。
         * <p>
         * 開始日は {@code yyyyMMdd} 形式で設定してください。
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
         * メソッドの呼び出しを行い開始日を設定してください。
         * <p>
         * 終了日は {@code yyyyMMdd} 形式で設定してください。
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
         * <p>
         * {@link #withStartDateAt(String)} メソッドと {@link #withEndDateAt(String)}
         * メソッドが呼び出され、開始日と終了日が設定されている場合は履歴リソースから為替情報を取得します。
         * 開始日と終了日が共に空文字列の場合は最新リソースから為替情報を取得します。
         *
         * @return {@link CurrencyExchangeRates} クラスの新しいインスタンス
         */
        public Communicable build() {

            final CurrencyExchangeRates api = new CurrencyExchangeRates();

            if (StringUtils.isEmpty(this.startAt) && StringUtils.isEmpty(this.endAt)) {
                api.resource = CurrencyExchangeRatesResource.LATEST;
            } else if (!StringUtils.isEmpty(this.startAt) && !StringUtils.isEmpty(this.endAt)) {
                api.resource = CurrencyExchangeRatesResource.HISTORY;
            } else {
                throw new InvalidDateException();
            }

            api.requestParameter = CurrencyExchangeRatesParameter.of(this.base.getTag(), this.getTsvSymbols(),
                    this.toDateWithHyphen(this.startAt), this.toDateWithHyphen(this.endAt));

            return api;
        }

        /**
         * 設定された検索対象のシンボルをHTTPリクエスト時に使用する際のTSV形式へ変換して文字列型として返却します。 検索対象のシンボルを設定する
         * {@link #withSymbolCurrencies(List)} メソッドが呼び出されなかった場合、または検索対象のシンボルが
         * {@code null} または空の場合は空文字列を返却します。空文字列が返却された場合は、後続処理の
         * {@link Communicable#createRequestParameter(RequestParameter)}
         * メソッドで当該パラメータの設定は除外されます。
         *
         * @return 検索対象のシンボルが設定されている場合はTSV形式へ変換されたシンボル。検索対象のシンボルが {@code null}
         *         または空の場合は空文字列
         */
        private String getTsvSymbols() {

            if (this.symbols == null || this.symbols.isEmpty()) {
                return "";
            }

            return String.join(",", this.symbols.stream().map(Currency::getTag).collect(Collectors.toList()));
        }

        /**
         * 引数として与えれられた {@code date} を {@code yyyy-MM-dd} 形式に変換し返却します。
         * <p>
         * 引数として渡される日付は {@code yyyyMMdd} 形式であることを想定しており、想定外の形式で日付が渡された場合は
         * {@link InvalidDateFormatException} が実行時に発生します。
         *
         * @param date 変換対象の日付（yyyyMMdd形式）
         * @return {@code yyyy-MM-dd} 形式に変換された日付
         *
         * @exception NullPointerException       引数として {@code null} が渡された場合
         * @exception InvalidDateFormatException 引数として渡された {@code date} が
         *                                       {@code yyyyMMdd} 形式ではない場合
         */
        private String toDateWithHyphen(@NonNull String date) {

            if (StringUtils.isEmpty(date)) {
                return "";
            }

            final SimpleDateFormat fromSdf = new SimpleDateFormat("yyyyMMdd");
            final SimpleDateFormat toSdf = new SimpleDateFormat("yyyy-MM-dd");

            try {
                return toSdf.format(fromSdf.parse(date));
            } catch (ParseException e) {
                throw new InvalidDateFormatException(e);
            }
        }
    }

    @Override
    public HttpResponse<String> send() {

        final String requestParameter = this.createQuery(this.requestParameter);
        final String requestUrl = String.format("%s/%s/%s", EXCHANGE_RATES_API, resource.getResource(),
                requestParameter);

        final HttpRequest request = HttpRequest.newBuilder().uri(URI.create(requestUrl)).GET().build();

        try {
            return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        } catch (IOException | InterruptedException e) {
            throw new ApiRequestFailedException(e);
        }
    }
}
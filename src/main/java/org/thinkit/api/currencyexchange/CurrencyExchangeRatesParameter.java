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

import java.io.Serializable;

import org.thinkit.api.common.annotation.ParameterMapping;
import org.thinkit.api.common.entity.RequestParameter;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 * 通貨為替レートAPIのリクエストパラメータを管理するクラスです。
 *
 * @author Kato Shinya
 * @since 1.0
 * @version 1.0
 */
@ToString
@EqualsAndHashCode
final class CurrencyExchangeRatesParameter implements RequestParameter, Serializable {

    /**
     * シリアルバージョンUID
     */
    private static final long serialVersionUID = 1039424269911235681L;

    /**
     * 基軸通貨
     */
    @Getter
    @ParameterMapping(key = "base")
    private String base;

    /**
     * 取得対象のシンボル
     */
    @Getter
    @ParameterMapping(key = "symbols")
    private String symbol;

    /**
     * 検索開始日
     */
    @Getter
    @ParameterMapping(key = "start_at")
    private String startAt;

    /**
     * 検索終了日
     */
    @Getter
    @ParameterMapping(key = "end_at")
    private String endAt;

    /**
     * デフォルトコンストラクタ
     */
    private CurrencyExchangeRatesParameter() {
    }

    /**
     * コンストラクタ
     *
     * @param base    基軸通貨
     * @param symbol  検索対象のシンボル
     * @param startAt 検索開始日
     * @param endAt   検索終了日
     *
     * @exception NullPointerException 引数として {@code null} が渡された場合
     */
    private CurrencyExchangeRatesParameter(@NonNull String base, @NonNull String symbol, @NonNull String startAt,
            @NonNull String endAt) {
        this.base = base;
        this.symbol = symbol;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    /**
     * コピーコンストラクタ
     *
     * @param currencyExchangeRatesParameter 通貨為替レートパラメータ
     *
     * @exception NullPointerException 引数として {@code null} が渡された場合
     */
    private CurrencyExchangeRatesParameter(@NonNull CurrencyExchangeRatesParameter currencyExchangeRatesParameter) {
        this.base = currencyExchangeRatesParameter.getBase();
        this.symbol = currencyExchangeRatesParameter.getSymbol();
        this.startAt = currencyExchangeRatesParameter.getStartAt();
        this.endAt = currencyExchangeRatesParameter.getEndAt();
    }

    /**
     * 引数として渡された情報を基に {@link CurrencyExchangeReatesParameter}
     * クラスの新しいインスタンスを生成し返却します。
     *
     * @param base    基軸通貨
     * @param symbol  検索対象のシンボル
     * @param startAt 検索開始日
     * @param endAt   検索終了日
     * @return {@link CurrencyExchangeReatesParameter} クラスの新しいインスタンス
     *
     * @exception NullPointerException 引数として {@code null} が渡された場合
     */
    public static RequestParameter of(@NonNull String base, @NonNull String symbol, @NonNull String startAt,
            @NonNull String endAt) {
        return new CurrencyExchangeRatesParameter(base, symbol, startAt, endAt);
    }

    /**
     * 引数として渡されたオブジェクトの情報を基に {@link CurrencyExchangeRetesParameter}
     * クラスの新しいインスタンスを生成し返却します。
     *
     * @param currencyExchangeRatesParameter 通貨為替レートパラメータ
     * @return {@link CurrencyExchangeRetesParameter} クラスの新しいインスタンス
     *
     * @exception NullPointerException 引数として {@code null} が渡された場合
     */
    public static RequestParameter of(@NonNull CurrencyExchangeRatesParameter currencyExchangeRatesParameter) {
        return new CurrencyExchangeRatesParameter(currencyExchangeRatesParameter);
    }
}

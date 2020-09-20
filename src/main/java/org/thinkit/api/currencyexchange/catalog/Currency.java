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

package org.thinkit.api.currencyexchange.catalog;

import org.thinkit.api.catalog.BiCatalog;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 為替通貨を管理するカタログです。
 *
 * @author Kato Shinya
 * @since 1.0
 * @version 1.0
 */
@RequiredArgsConstructor
public enum Currency implements BiCatalog<Currency, String> {

    /**
     * アイスランド・クローナ
     */
    ICELAND_KRONA(0, "ISK"),

    /**
     * フィリピン・ペソ
     */
    PHILIPPHINE_PESO(1, "PHP"),

    /**
     * デンマーク・クローネ
     */
    DANISH_KRONE(2, "DKK"),

    /**
     * カナダ・ドル
     */
    CANADIAN_DOLLAR(3, "CAD"),

    /**
     * 香港ドル
     */
    HONG_KONG_DOLLAR(4, "HKD"),

    /**
     * ハンガリー・フォリント
     */
    HUNGARIAN_FORINT(5, "HUF"),

    /**
     * チェコ・コルナ
     */
    CZECH_KORUNA(6, "CZK"),

    /**
     * 豪ドル
     */
    AUSTRALIAN_DOLLAR(7, "AUD"),

    /**
     * ルーマニア・レウ
     */
    ROMANIAN_LEU(8, "RON"),

    /**
     * スウェーデン・クローナ
     */
    SWEDISH_KRONA(9, "SEK"),

    /**
     * インドネシア・ルピー
     */
    INDONESIAN_RUPEE(10, "IDR"),

    /**
     * インド・ルピー
     */
    INDIAN_RUPEE(11, "INR"),

    /**
     * ブラジル・レアル
     */
    BRAZILIAN_REAL(12, "BRL"),

    /**
     * ロシア・ルーブル
     */
    RUSSIAN_RUBLE(13, "RUB"),

    /**
     * クロアチア・クーナ
     */
    CROATIAN_KUNA(14, "HRK"),

    /**
     * 日本円
     */
    JAPANESE_YEN(15, "JPY"),

    /**
     * タイ・バーツ
     */
    THAILAND_BAHT(16, "THB"),

    /**
     * スイス・フラン
     */
    SWISS_FRANC(17, "CHF"),

    /**
     * シンガポール・ドル
     */
    SINGAPORE_DOLLAR(18, "SGD"),

    /**
     * ポーランド・ズウォティ
     */
    POLISH_ZLOTY(19, "PLN"),

    /**
     * ブルガリア・レフ
     */
    BULGARIAN_LEV(20, "BGN"),

    /**
     * トルコ・リラ
     */
    TURKISH_LIRA(21, "TRY"),

    /**
     * 中華人民元
     */
    CHINESE_YUAN(22, "CNY"),

    /**
     * ノルウェー・クローネ
     */
    NORWEGIAN_KRONE(23, "NOK"),

    /**
     * ニュージーランド・ドル
     */
    NEW_ZEALAND_DOLLAR(24, "NZD"),

    /**
     * 南アフリカ・ランド
     */
    SOUTH_AFRICAN_RAND(25, "ZAR"),

    /**
     * 米国ドル
     */
    USA_DOLLAR(26, "USD"),

    /**
     * メキシコ・ペソ
     */
    MEXICAN_PESO(27, "MXN"),

    /**
     * イスラエル・新シェケル
     */
    ISRAELI_NEW_SHEKEL(28, "ILS"),

    /**
     * 英国スターリング・ポンド
     */
    ENGLAND_STERLING_POUND(29, "GBP"),

    /**
     * 韓国ウォン
     */
    KOREAN_WON(30, "KRW"),

    /**
     * マレーシア・リンギット
     */
    MALAYSIAN_RINGGIT(31, "MYR");

    /**
     * コード値
     */
    @Getter
    private final int code;

    /**
     * タグ
     */
    @Getter
    private final String tag;
}

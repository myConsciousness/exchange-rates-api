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

import org.thinkit.api.common.Resource;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 為替レートAPIで提供されているリソースを管理するクラスです。
 *
 * @author Kato Shinya
 * @since 1.0
 * @version 1.0
 */
@RequiredArgsConstructor
enum CurrencyExchangeRatesResource implements Resource {

    /**
     * 最新の為替情報を取得するリソース
     */
    LATEST("latest"),

    /**
     * 履歴から為替情報を取得するリソース
     */
    HISTORY("history");

    /**
     * リソース
     */
    @Getter
    private final String resource;
}

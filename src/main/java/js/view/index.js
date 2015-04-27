/* 
 * Copyright 2015 Marcel.Barbosa.
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

load(jsBaseDir + '/js/view/helper.js');
load(jsBaseDir + '/js/lib/underscore-min.js');


var name = context.getParameters().get("name").toString();

var compiled = _.template(Helper.readContents(jsBaseDir + "/js/view/template/index.html"));

output.write(compiled({name : name, ret: "Underscore Template", tpl : "Yeah!"}));
/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.cancai.demo.imageloaderdemo;

/**
 * 存放常量
 *
 * @author peter_wang
 * @create-time 15/7/6 14:07
 */
public final class Constants {

	public static final String[] IMAGES = new String[] {
			// Heavy images
			"http://e.hiphotos.baidu.com/image/w%3D310/sign=9a7b040b0c2442a7ae0efba4e142ad95/4bed2e738bd4b31c832baa3384d6277f9e2ff826.jpg",
			"http://g.hiphotos.baidu.com/image/w%3D310/sign=0fd491895f6034a829e2be80fb1149d9/d000baa1cd11728bd1e194aacbfcc3cec2fd2c6d.jpg",
			"http://h.hiphotos.baidu.com/image/w%3D310/sign=a4d5d655a864034f0fcdc4079fc27980/b999a9014c086e06f2bb27c301087bf40ad1cb23.jpg",
			"http://f.hiphotos.baidu.com/image/w%3D310/sign=12fa05801c30e924cfa49a307c0a6e66/7acb0a46f21fbe097e91db9c68600c338644ad6e.jpg",
			"http://d.hiphotos.baidu.com/image/w%3D310/sign=32569f0fd3a20cf44690f8de46084b0c/e1fe9925bc315c605f7c73678eb1cb1348547748.jpg",
			"http://b.hiphotos.baidu.com/image/w%3D310/sign=43b7dc6b542c11dfded1b92253266255/d62a6059252dd42a095a1a23003b5bb5c8eab8e2.jpg",
			"http://h.hiphotos.baidu.com/image/w%3D310/sign=18e9f066b78f8c54e3d3c32e0a282dee/a686c9177f3e67097e85737738c79f3df9dc55b4.jpg",
			"http://d.hiphotos.baidu.com/image/w%3D2048/sign=1b1f93ad533d26972ed30f5d61c3b3fb/023b5bb5c9ea15ce8d204e6eb4003af33b87b28f.jpg",
			"http://f.hiphotos.baidu.com/image/w%3D310/sign=055b536aadc379317d688028dbc5b784/1c950a7b02087bf4777ac685f0d3572c10dfcfcc.jpg",
			"http://a.hiphotos.baidu.com/image/w%3D310/sign=052184fd53da81cb4ee685cc6266d0a4/cefc1e178a82b901e2dfaf3a718da9773912ef4d.jpg",
			"http://h.hiphotos.baidu.com/image/w%3D310/sign=c0160301272dd42a5f0907aa333a5b2f/7dd98d1001e939013603db5679ec54e736d19650.jpg",
			"http://e.hiphotos.baidu.com/image/w%3D310/sign=f72912cfb0119313c743f9b155390c10/a6efce1b9d16fdfa0b3b1ae3b68f8c5494ee7b6c.jpg",
			"http://b.hiphotos.baidu.com/image/w%3D310/sign=d63c1e2a37fae6cd0cb4ad603fb30f9e/b151f8198618367a9ba380c02c738bd4b31ce581.jpg",
			"http://g.hiphotos.baidu.com/image/w%3D310/sign=11471c000855b3199cf9847473a88286/03087bf40ad162d94c57321d13dfa9ec8a13cd72.jpg",
			"http://e.hiphotos.baidu.com/image/w%3D310/sign=fa176dfe7ed98d1076d40a30113eb807/0823dd54564e925805c37dec9e82d158ccbf4e39.jpg",
			"http://e.hiphotos.baidu.com/image/w%3D310/sign=d70ef1b3d358ccbf1bbcb33b29d9bcd4/8694a4c27d1ed21b6a2476e4af6eddc450da3fc5.jpg",
			"http://g.hiphotos.baidu.com/image/w%3D310/sign=6e5f5d7bf9f2b211e42e834ffa816511/77c6a7efce1b9d16839da169f1deb48f8c54645b.jpg",
			"http://a.hiphotos.baidu.com/image/w%3D310/sign=72ae5c21b27eca8012053fe6a1229712/9c16fdfaaf51f3de7430436696eef01f3a297936.jpg",
			"http://c.hiphotos.baidu.com/image/w%3D310/sign=405137dfeb50352ab16123096342fb1a/d4628535e5dde711ea4d7b9ea5efce1b9d16613e.jpg",
			"http://d.hiphotos.baidu.com/image/w%3D310/sign=5fd1be5d36a85edffa8cf822795509d8/bba1cd11728b47105af22ac1c1cec3fdfd0323da.jpg",
			"http://g.hiphotos.baidu.com/image/w%3D310/sign=c7629c715b82b2b7a79f3fc501adcb0a/3b292df5e0fe9925da176d2536a85edf8db171af.jpg",
			"http://d.hiphotos.baidu.com/image/pic/item/fd039245d688d43fe4fdce997f1ed21b0ef43b00.jpg",
			"http://c.hiphotos.baidu.com/image/pic/item/3b292df5e0fe99256e1e993736a85edf8db17143.jpg",
			"http://b.hiphotos.baidu.com/image/pic/item/241f95cad1c8a7862834a8686509c93d70cf50be.jpg",
			"http://b.hiphotos.baidu.com/image/pic/item/48540923dd54564ef35c9145b1de9c82d1584f94.jpg",
			"http://e.hiphotos.baidu.com/image/pic/item/64380cd7912397dd050468635b82b2b7d0a2875c.jpg",
			"http://g.hiphotos.baidu.com/image/pic/item/3801213fb80e7becf2dbe1872d2eb9389b506b57.jpg",
            "http://img0.imgtn.bdimg.com/it/u=3045733783,3374698011&fm=21&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=3045733783,3374698011&fm=21&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=3045733783,3374698011&fm=21&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=3045733783,3374698011&fm=21&gp=0.jpg",
	};

	private Constants() {
	}
}

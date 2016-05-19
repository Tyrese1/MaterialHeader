
<h1 align="center">MaterialHeader</h1>
<h4 align="center">Android Library</h4>
<p align="center">Android UI Library For Quick Image ViewPager With Image Fast Loading and Caching For Making Material Header</p>
#MaterialHeader
![](https://github.com/Elbehiry/MaterialHeader/blob/master/screenshots/remote.gif)
![](https://github.com/Elbehiry/MaterialHeader/blob/master/screenshots/local.gif)

#Gradle Dependency

###Repository
The Gradle dependency is available via [jCenter](https://bintray.com/drummer-aidan/maven/material-dialogs/view).
jCenter is the default Maven repository used by Android Studio.


```groovy
dependencies {
	// ... other dependencies here

	compile 'com.elbehiry.header:header:1.0'
}
```

#Maven Dependency
```groovy
	<dependency>
 	<groupId>com.elbehiry.header</groupId> 
	<artifactId>header</artifactId>
	<version>1.0</version> 
	<type>pom</type> 
	</dependency>
```


##Usage
###add viewpager in your xml layout ,and viewgroup of circles



```xml
	<android.support.v4.view.ViewPager
	android:id="@+id/image_slide_page" 
	android:layout_width="match_parent" 
	android:layout_height="match_parent" 
	android:focusable="true"/>
    	/>
```
```xml
	<LinearLayout 
	android:id="@+id/layout_circle_images"
	android:layout_width="match_parent"
	android:layout_height="match_parent"
	android:gravity="center"
	android:layout_centerVertical="true"
	android:orientation="horizontal">
	</LinearLayout>
```
###add default array of images, or array of links you want to fetech!

```Java
	int [] images = {
            R.drawable.image1,
            R.drawable.image2,
            R.drawable.image3,
            R.drawable.image4
	    };
        String [] image_links = {
            "http://imgur.com/download/55IkcVP",
            "http://imgur.com/download/5rtoa9S",
            "http://imgur.com/download/BilVc1r",
            "http://imgur.com/download/vRrmZO1"
    	};
```
set if you want to auto moved...
```Java
    	boolean adAuto = true;
```
--------------------------------------------------------------------------------

```Java
	ViewPager viewPager = (ViewPager) findViewById(R.id.image_slide_page);
        ViewGroup  imageCircleView = (ViewGroup) findViewById(R.id.layout_circle_images);
```

```Java
        new MaterialHeader.Builder(this)
                .PagerView(viewPager)   //image container
		.CircleContainer(imageCircleView)   //circle container
		.TimeDuration(3000)   //duration 
		.ImagesLinks(image_links)  //links of images you want to fetch
		.DefaultImages(images)  //your default drawable images
		.autoAd(adAuto) //auto moves	
		.init();  //start!

```


## License
	Copyright 2016 Mohamed Elbehiry

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	   http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.




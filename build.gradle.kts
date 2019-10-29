buildscript {
    repositories {
        addRepos(repositories)
    }

    dependencies {
        classpath(BuildPlugins.androidGradlePlugin)
        classpath(BuildPlugins.kotlinGradlePlugin)
        classpath("com.novoda:bintray-release:0.9")
    }
}

allprojects {
    repositories {
        addRepos(repositories)
        flatDir {
            dirs("libs")
        }
        maven { setUrl("http://www.guuguo.top/") }
    }
}
tasks.register("clean").configure {
    delete("build")
}




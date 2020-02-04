#ifndef LAUNCHAPPLICATION_H
#define LAUNCHAPPLICATION_H


#include <QGuiApplication>
#include <QQmlApplicationEngine>

#include <QQmlContext>

//add
#include <QAndroidJniEnvironment>
#include <jni.h>
#include <QtAndroidExtras>
#include <QAndroidJniObject>
#include <QtAndroid>

class launchApplication : public QObject{
    Q_OBJECT

public:
    //method 1
    Q_INVOKABLE void startApplication(const QString &packageName, const QString &className){
        qDebug("lkb cpp launch app");
        QAndroidJniObject::callStaticMethod<void>("com.dynavin.example.AndroidApi", //class name
                                                  "launchApplication", //function name
                                                  "(Ljava/lang/String;Ljava/lang/String;)V",
                                                  QAndroidJniObject::fromString(packageName).object<jstring>(),
                                                  QAndroidJniObject::fromString(className).object<jstring>());
    }
    //method 2, simple
    Q_INVOKABLE void startCam(){
        qDebug("lkb start cam app");
        QAndroidJniObject packageName = QAndroidJniObject::fromString("com.rockchip.car.recorder");
        QAndroidJniObject className = QAndroidJniObject::fromString("com.rockchip.car.recorder.activity.IndexActivity");

        QAndroidJniObject intent("android/content/Intent", "()V"); //construct a object based class name,likes "new Intent()"
        intent.callObjectMethod("setClassName", //method name,setClassName(String packageName, String className)
                                "(Ljava/lang/String;Ljava/lang/String;)Landroid/content/Intent;", //signature
                                packageName.object<jstring>(),
                                className.object<jstring>());
        QtAndroid::startActivity(intent, 0);
    }
};

#endif // LAUNCHAPPLICATION_H

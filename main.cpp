#include <QGuiApplication>
#include <QQmlApplicationEngine>

#include <QQmlContext>
#include "launchapplication.h"


int main(int argc, char *argv[])
{
    QGuiApplication app(argc, argv);

    QQmlApplicationEngine engine;
    engine.load(QUrl(QStringLiteral("qrc:/main.qml")));
    engine.rootContext()->setContextProperty("launchApp", new launchApplication);

    if (engine.rootObjects().isEmpty())
        return -1;

    return app.exec();
}

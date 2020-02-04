import QtQuick 2.6
import QtQuick.Window 2.2

Window {
    visible: true
    width: 500
    height: 400
    title: qsTr("Hello World")

    Rectangle {
        anchors.centerIn: parent
        width: 300
        height: 300
        color: "green"

        Text {
            text: "start back camera"
            anchors.centerIn: parent
            font.pointSize: 25
        }

        MouseArea {
            id: mouseArea
            anchors.fill: parent
            onClicked: {
                console.log(qsTr('Clicked on background'))
                //launchApp.startApplication("com.rockchip.car", "com.rockchip.car.recorder"); //method 1
                launchApp.startCam(); //method 2
            }
        }
    }
}

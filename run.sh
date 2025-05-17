#!/usr/bin/env bash
FXPATH="$HOME/libraries/javafx-sdk-24/lib"
MODPATH="src/target/classes"
APPMODULE="com.jawa"
MAINCLASS="com.jawa.App"

java --module-path "$FXPATH":"$MODPATH" \
     --add-modules javafx.controls,javafx.fxml,"$APPMODULE" \
     -m "$APPMODULE"/"$MAINCLASS"

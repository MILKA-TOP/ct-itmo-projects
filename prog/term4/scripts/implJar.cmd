cd ..
javac -d impl_dir -cp ../java-advanced-2022/artifacts/info.kgeorgiy.java.advanced.implementor.jar java-solutions/info/kgeorgiy/ja/milenin/implementor/Implementor.java
cd impl_dir
jar cfm ./../Implementor.jar ./../java-solutions/META-INF/MANIFEST.MF info/
cd ..
rd /s /q impl_dir
pause
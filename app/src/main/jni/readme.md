#Readme

gettext与glib是使用sdcv所需要的库，由于太复杂，直接使用ndk-build需要大量的修改工作，因此直接使用NDK导出的交叉编译工具链，其中configure选项如下

####gettext

> ./configure --host=arm-linux   CC=arm-linux-androideabi-gcc CXX=arm-linux-androideabi-g++ LD=arm-linux-androideabi-ld RANLIB=arm-linux-androideabi-ranlib AR=arm-linux-androideabi-ar  --disable-java --disable-native-java --disable-threads  --enable-shared --disable-libtool-lock  --disable-nls --disable-rpath  --disable-c++ --disable-libasprintf --disable-acl --disable-openmp --disable-curses --without-libiconv-prefix --without-libglib-2.0-prefix --without-emacs --without-git --without-bzip2 --without-xz 

####glib

> ./configure --host=arm-linux-androideabi-gcc   CC=arm-linux-androideabi-gcc CXX=arm-linux-androideabi-g++ LD=arm-linux-androideabi-ld RANLIB=arm-linux-androideabi-ranlib AR=arm-linux-androideabi-ar    --disable-maintainer-mode --disable-mem-pools --disable-rebuilds --disable-libtool-lock --disable-selinux --disable-fam --disable-xattr --disable-libelf --disable-Bsymbolic --disable-znodelete --disable-compile-warnings --with-libiconv=no 

使用这两个我修改过的l库，尽管如此，两个库都不能成功编译，好在不需要生成所有的库，gettext会提示找不到-lpthread(android使用了bionic C 因此找到相应的Makefile改为-lc)。然后重新make到失败，需要的动态库已经编译成功。目标一般在相应目录的.lib(linux下是隐藏文件夹)内，分别找到gnuintl.so与glib-2.0.so即可。最好strip一下来减少动态库的体积。生成的动态库可以直接在应用中使用。

> 是用android studio编译yykdict需要把预编译的so文件放到NDK的platform里


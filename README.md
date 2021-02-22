## CKM Mirror

> Tools to store the latest OpenEHR ckm archetypes and templates and provide corresponding download service

1. download the latest OpenEHR ckm archetypes and templates.

    - The url of CKMRepository is stored in a configuration file.
    - the local dir that stores the archetypes and templates.
    
第一次运行，创建ckm-mirror数据库，建表;
从ckm下载原型和模板的信息存入数据库；
根据信息中的地址下载所有的原型和模板，存入相应的目录。


以后运行时，间隔固定时间，从ckm读取所有的原型信息，依次对每个原型作如下处理：
- 取得其archetype_id；
- 从数据库表中的原型表中查询这个archetype_id;
    - 若表中不存在这个id的原型记录：
        - a. 将这条记录存入原型表；
        - b. 下载该原型存入相应的目录；
    - 若表中存在这个id的原型记录：
        - a. 获取这条记录中的更新时间；
        - b. 将这个时间与ckm上的原型记录的更新时间对比，
             若不相同（此时ckm更新时间应晚于本地数据库
             中的更新时间），从对应的下载地址下载最新的
             原型存入相应目录（替换原来的原型文件）；
             若相同，则不下载。
        - c. 利用读到的ckm上的原型记录信息更新本地数据库表。




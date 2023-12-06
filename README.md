# PrintAddress

### 使い方
#### 個別に用意する
- sqliteのDBファイル
- 日本語対応フォント

#### 設定
##### 環境変数
```
export PDF_OUT_DIR=PDF出力ディレクトリへのパス
export ADDRESS_SQLITE=SQLITEファイルへのパス
export PDF_FONT=フォントファイルへのパス
```

##### テーブル
- 宛先テーブル
```
create table to_info
(
    id               integer
        constraint to_info_pk
            primary key,
    zip_code         text,
    address1         text,
    address2         text,
    family_name      text,
    first_names      text,
    honorific_title1 text,
    not_send         integer default 0 not null
);
```
- 差出人テーブル
```
create table from_info
(
    id           integer
        constraint to_info_pk
            primary key,
    zip_code     text,
    address1     text,
    address2     text,
    family_name  text,
    in_use       integer default 0,
    phone_number text,
    first_names  text
);
// in_useを1に指定したレコードを使用する（複数ある場合は取得順の1件目）
```
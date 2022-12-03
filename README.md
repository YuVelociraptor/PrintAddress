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
create table main.to_info
(
    id       INTEGER
        constraint to_info_pk
            primary key,
    zip_code TEXT,
    address1 TEXT,
    address2 TEXT,
    name     TEXT
);
```
- 差出人テーブル
```
create table main.from_info
(
    id       integer
        constraint to_info_pk
            primary key,
    zip_code text,
    address1 text,
    address2 text,
    name     text,
    in_use   integer default 0
);
// in_useを1に指定したレコードを使用する（複数ある場合は取得順の1件目）
```
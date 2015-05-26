# talk-system-network

### args
* 第一引数: 8000 のようにポートを指定
 
### get request
* (/shiritori\?keyword=)(.+)(&mode=)(.+) この正規表現にあてはまるものでアクセス
 
### response
* get requestに指定されてない正規表現が来るとレスポンスはなし(error処理書いてない)

### develop
* 実装済み：ランダムレスポンス
* 未実装：siritoriレスポンス

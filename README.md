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

### process kill
errorが発生しない限り延々とメモリを食い続けますので、以下コマンド使って切って下さい
```.sh
ps -ef | grep java | grep talk-system 
kill -9 PID番号いれてもらう
```

# 変更点
* Json形式で返す
* modeに指定された値が間違っていた場合のcsvを追加
* ランダムレスポンスのレコードの数はいくつでも処理するように修正

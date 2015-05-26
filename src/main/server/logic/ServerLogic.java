package main.server.logic;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.URLDecoder;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Created by os on 2015/05/26.
 */
public class ServerLogic {
    private Socket _socket = null;
    private ServerSocket _serverSocket = null;
    private BufferedReader _reader = null;
    private PrintStream _writer = null;
    private BufferedReader _br = null;

    private static final String FORMAT_REGEX = "(/shiritori\\?keyword=)(.+)(&mode=)(.+)";
    private static final int OUTPUT_COLUMN = 1;

    private enum Mode {
        ANIME("anime"),SHUZO("shuzo"),LYLICS("lylics");
        String mode;
        Mode(String modeStr) {
            mode = modeStr;
        }
    }

    /**
     * 受け取ったメッセージをそのまま返すサーバ
     * @param _port
     * @throws IOException
     */
    protected void run(String _port) throws IOException {
        try {
            _serverSocket = new ServerSocket(parseInt(_port));

            while (true) {
                init();

                String line = _reader.readLine();
                if (line != null && !"".equals(line)) {
                    String requestPath = URLDecoder.decode(line.split(" ")[1], "UTF-8");
                    Matcher m = Pattern.compile(FORMAT_REGEX).matcher(requestPath);
                    if (m.find()) {
                        final String keyword = rmExtraChar(m.group(2));
                        final String mode = rmExtraChar(m.group(4));
                        if (keyword != null && !"".equals(keyword)
                                && mode != null && !"".equals(mode)) {
                            selectMode(mode);
                            outputWord(keyword);
                        }
                    }
                }
            }
        } catch (SocketException e) {
            systemExit("Socket error");
        } catch (IOException e) {
            systemExit("IO error");
        } finally {
            close();
        }
    }

    /**
     * モードによってテキストを切り分け
     * @param mode
     * @throws FileNotFoundException
     */
    private void selectMode(String mode) throws FileNotFoundException {
        if (Mode.ANIME.mode.equals(mode)) {
            _br = new BufferedReader(new FileReader("/Users/shn/workspace/java/talk-system/src/main/resource/anime.csv"));
        } else if (Mode.SHUZO.mode.equals(mode)) {
            _br = new BufferedReader(new FileReader("/Users/shn/workspace/java/talk-system/src/main/resource/shuzo.csv"));
        } else if (Mode.LYLICS.mode.equals(mode)) {
            _br = new BufferedReader(new FileReader("/Users/shn/workspace/java/talk-system/src/main/resource/lylics.csv"));
        }
    }

    /**
     * 出力の実体部分
     * @param targetWord
     * @throws IOException
     */
    private void outputWord(String targetWord) throws IOException {
        String read = null;
        StringTokenizer token;
        int rand = new Random().nextInt(10);
        int i = 0;
        while ((read = _br.readLine()) != null) {
            if (rand == i) {
                token = new StringTokenizer(read, ",");
                int j = 0;
                while (token.hasMoreTokens()) {
                    String column = token.nextToken();
                    if (j == OUTPUT_COLUMN)
                        output(column);
                    j++;
                }
            }
            i++;
        }
    }

    /**
     * 初期処理
     * @throws IOException
     */
    private void init() throws IOException {
        _socket = _serverSocket.accept();
        _reader = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
        _writer = new PrintStream(_socket.getOutputStream());
    }

    /**
     * 無駄なキャラセットを弾く
     * @param word
     * @return
     */
    private String rmExtraChar(String word) {
        return word.replaceAll("[、。？！ 　]", "");
    }

    /**
     * 出力
     * @param message
     */
    private void output(String message) {
        System.out.println(message);
        _writer.println(message);
        _writer.flush();
    }

    /**
     * 閉じる
     * @throws IOException
     */
    private void close() throws IOException {
        _writer.close();
        _reader.close();
        _br.close();
        _socket.close();
        _serverSocket.close();
    }

    /**
     * 指定されたメッセージを出力後、-1コードでexitする
     * @param errMsg
     */
    private void systemExit(String errMsg) {
        System.err.println(errMsg);
        System.exit(-1);
    }

    /**
     * 指定された文字列を数値に変換します
     * @param _port
     * @return
     */
    private Integer parseInt(String _port) {
        try {
            return Integer.parseInt(_port);
        } catch (Exception e) {
            systemExit("Port not found");
            throw new IllegalArgumentException();
        }
    }
}

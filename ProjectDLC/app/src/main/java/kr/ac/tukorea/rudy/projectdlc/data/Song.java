package kr.ac.tukorea.rudy.projectdlc.data;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.util.JsonReader;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.BufferedReader;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;

import kr.ac.tukorea.rudy.projectdlc.game.Note;

public class Song {
    //////////////////////////////////////////////////
    /// from songs.json
    public int rank;
    public String title, artist, token;
    public int bpm;
    public int sun, twi, mid, aby, tre;
    //////////////////////////////////////////////////

    protected Bitmap bitmap;
    public ArrayList<Note> notes;

    private static final String TAG = Song.class.getSimpleName();

    public static ArrayList<Song> songs = new ArrayList<>();
    private static AssetManager assetManager;
    private MediaPlayer mediaPlayer;
    private boolean finished = false;
    public int noteIndex;

    public static ArrayList<Song> load(Context context, String filename) {
        songs = new ArrayList<>();
        try {
            assetManager = context.getAssets();
            InputStream is = assetManager.open(filename);
            JsonReader jr = new JsonReader(new InputStreamReader(is));
            jr.beginArray();
            while (jr.hasNext()) {
                Song song = loadSong(jr);
                if (song != null) {
                    songs.add(song);
                    Log.d(TAG, "Songs count =" + songs.size() + " " + song);
                }
            }
            jr.endArray();
            jr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return songs;
    }

    public static void unload() {
        songs.clear();
        assetManager = null;
    }

    public static Song get(int index) {
        return songs.get(index);
    }

    private static Song loadSong(JsonReader jr) {
        Song song = new Song();
        try {
            jr.beginObject();
            while (jr.hasNext()) {
                String name = jr.nextName();
                if (!JsonHelper.readProperty(song, name, jr)) {
                    jr.skipValue();
                }
            }
            jr.endObject();
        } catch (IOException e) {
            return null;
        }
        return song;
    }

    @NonNull
    @Override
    public String toString() {
        return "Song:<" +  title + "/" + artist + "/" + token + ">";
    }

    public String getTitle() {
        return title;
    }

    public Bitmap getBitmap() {
        if (bitmap != null) {
            return bitmap;
        }

        try {
            bitmap = BitmapFactory.decodeStream(assetManager.open("Songs/" + token + "/cover.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public void loadNotes(String diff) {
        if (notes != null && !notes.isEmpty()) return;

        notes = new ArrayList<>();
        noteIndex = 0;

        float lengthInBar = 0;
        try {
            InputStream is = assetManager.open("Songs/" + token + "/" + diff + ".txt");
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isr);
            while (true) {
                String line = reader.readLine();
                if (line == null) break;
                Note note = Note.parse(line, bpm);
                if (note == null) continue;
                notes.add(note);
                if (lengthInBar < note.bar) {
                    lengthInBar = note.bar;
                }
            }
            is.close();
            Log.d(TAG, "Song loaded: " + notes.size() + " notes, " + lengthInBar + " seconds.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Note popNoteBefore(float musicTime) {
        if (noteIndex >= notes.size()) return null;
        Note note = notes.get(noteIndex);
        if (note.bar > musicTime) return null;
        //Log.d(TAG, "Popping nodeIndex=" + noteIndex);
        noteIndex++;
        return note;
    }


    public void playDemo() {
        stop();
        try {
            AssetFileDescriptor afd = assetManager.openFd("Songs/" + token + "/preview.mp3");
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(afd);
            mediaPlayer.prepare();
            mediaPlayer.start();
            Log.d(TAG, "Playing " + title + "/" + artist);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isFinished() {
        return finished;
    }

    public void stop() {
        if (mediaPlayer != null) {
            Log.d(TAG, "Stopping " + title + "/" + artist);
            mediaPlayer.stop();
            mediaPlayer = null;
        }
    }

    public void play() {
        //loadNotes();
        stop();
        try {
            AssetFileDescriptor afd = assetManager.openFd("Songs/" + token + "/music.mp3");
            FileDescriptor fd = afd.getFileDescriptor();
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(fd, afd.getStartOffset(), afd.getLength());
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(mp -> {
                finished = true;
                Log.d(TAG, "Music finished.");
            });
            mediaPlayer.start();
            Log.d(TAG, "Play: " + this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        if (mediaPlayer == null) return;
        mediaPlayer.pause();
    }

    public void resume() {
        if (mediaPlayer == null) return;
        mediaPlayer.start();
    }


}

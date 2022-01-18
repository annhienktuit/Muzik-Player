package com.annhienktuit.muzikplayer.fragments

import android.annotation.SuppressLint
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.annhienktuit.muzikplayer.R
import android.provider.MediaStore
import android.util.Log
import com.annhienktuit.muzikplayer.models.LocalTrack
import java.io.File

class LocalMusicFragment : Fragment() {

    private var listLocalSong = ArrayList<File>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_local_music, container, false)
//        listLocalSong = getListFiles(Environment.getExternalStorageDirectory())
//        for(file in listLocalSong){
//            Log.i("Nhiennha ",file.path)
//        }
        return view
    }

    @SuppressLint("Range")
    fun scanMusic(){
        val uri: Uri = Uri.parse(Environment.getExternalStorageDirectory().toString())
        val selection = MediaStore.Audio.Media.IS_MUSIC + "!=0"
        val cursor = requireContext().contentResolver.query(uri, null, selection, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val name: String =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME))
                    val artist: String =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                    val url: String =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                    val s = LocalTrack(name, artist, url)
                } while (cursor.moveToNext())
            }
            cursor.close()
        }
    }

    private fun getListFiles(parentDir: File): ArrayList<File> {
        val inFiles = ArrayList<File>()
        val files = parentDir.listFiles()
        for (file in files) {
            if (file.isDirectory) {
                inFiles.addAll(getListFiles(file))
            }
        }
        return inFiles
    }

}
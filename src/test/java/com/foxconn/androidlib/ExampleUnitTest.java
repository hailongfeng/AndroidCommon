package com.foxconn.androidlib;

import com.foxconn.androidlib.test.http.RetrofitModel;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
      String blog=  RetrofitModel.getInstance().getCsdnBlog();
      assertNotNull(blog);
    }
}
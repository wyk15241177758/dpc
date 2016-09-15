package com.jt.gateway.util;

import java.io.File;

public class FileUtil {
	/**
     * �ݹ�ɾ��Ŀ¼�µ������ļ�����Ŀ¼�������ļ�
     * @param dir ��Ҫɾ�����ļ�Ŀ¼
     * @return boolean Returns "true" if all deletions were successful.
     *                 If a deletion fails, the method stops attempting to
     *                 delete and returns "false".
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //�ݹ�ɾ��Ŀ¼�е���Ŀ¼��
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // Ŀ¼��ʱΪ�գ�����ɾ��
        return dir.delete();
    }
}

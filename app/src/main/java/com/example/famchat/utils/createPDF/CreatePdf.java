package com.example.famchat.utils.createPDF;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.example.famchat.utils.createPDF.model.ImageToPDFOptions;
import com.example.famchat.utils.createPDF.model.Watermark;
import com.example.famchat.utils.createPDF.model.WatermarkPageEvent;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * An async task that converts selected images to Pdf
 */
public class CreatePdf extends AsyncTask<String, String, String> {

    private final String mFileName;
    private final String mPassword;
    private final String mQualityString;
    private final ArrayList<String> mImagesUri;
    private final int mBorderWidth;
    private final OnPDFCreatedInterface mOnPDFCreatedInterface;
    private boolean mSuccess;
    private String mPath;
    private final String mPageSize;
    private final boolean mPasswordProtected;
    private Boolean mWatermarkAdded;
    private Watermark mWatermark;
    private int mMarginTop;
    private int mMarginBottom;
    private int mMarginRight;
    private int mMarginLeft;
    private String mImagescaleType;
    private String mPageNumStyle;
    private String mMasterPwd;
    private int mPageColor;
    private Context context;

    public CreatePdf(Context context, ImageToPDFOptions mImageToPDFOptions, String parentPath,
                     OnPDFCreatedInterface onPDFCreated) {
        this.context = context;
        this.mImagesUri = mImageToPDFOptions.getImagesUri();
        this.mFileName = mImageToPDFOptions.getOutFileName();
        this.mPassword = mImageToPDFOptions.getPassword();
        this.mQualityString = mImageToPDFOptions.getQualityString();
        this.mOnPDFCreatedInterface = onPDFCreated;
        this.mPageSize = mImageToPDFOptions.getPageSize();
        this.mPasswordProtected = mImageToPDFOptions.isPasswordProtected();
        this.mBorderWidth = mImageToPDFOptions.getBorderWidth();
        this.mWatermarkAdded = mImageToPDFOptions.isWatermarkAdded();
        this.mWatermark = mImageToPDFOptions.getWatermark();
        this.mMarginTop = mImageToPDFOptions.getMarginTop();
        this.mMarginBottom = mImageToPDFOptions.getMarginBottom();
        this.mMarginRight = mImageToPDFOptions.getMarginRight();
        this.mMarginLeft = mImageToPDFOptions.getMarginLeft();
        this.mImagescaleType = mImageToPDFOptions.getImageScaleType();
        this.mPageNumStyle = mImageToPDFOptions.getPageNumStyle();
        this.mMasterPwd = mImageToPDFOptions.getMasterPwd();
        this.mPageColor = mImageToPDFOptions.getPageColor();
        mPath = parentPath;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mSuccess = true;
        mOnPDFCreatedInterface.onPDFCreationStarted();
    }

    @Override
    protected String doInBackground(String... params) {
        File folder = new File(mPath);
        if (!folder.exists())
            folder.mkdir();
        mPath = mPath + mFileName + ".pdf";
        Document document = null;
        try {
            for (int i = 0; i < mImagesUri.size(); i++) {
                int quality;
                quality = 100;
                if (!TextUtils.isEmpty(mQualityString)) {
                    quality = Integer.parseInt(mQualityString);
                }
                Image image = Image.getInstance(mImagesUri.get(i));
                double qualtyMod = quality * 0.08;
                image.setCompressionLevel((int) qualtyMod);
                image.setBorder(Rectangle.BOX);
                image.setBorderWidth(mBorderWidth);

                Rectangle pageSize = new Rectangle(image.getWidth(), image.getHeight());
                pageSize.setBackgroundColor(getBaseColor(mPageColor));
                document = new Document(pageSize,
                        mMarginLeft, mMarginRight, mMarginTop, mMarginBottom);
                document.setMargins(mMarginLeft, mMarginRight, mMarginTop, mMarginBottom);
                Rectangle documentRect = document.getPageSize();
                PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(mPath));

                if (mPasswordProtected) {
                    writer.setEncryption(mPassword.getBytes(), PdfLib.DEFAULT_MASTER_PW.getBytes(),
                            PdfWriter.ALLOW_PRINTING | PdfWriter.ALLOW_COPY,
                            PdfWriter.ENCRYPTION_AES_128);

                }

                if (mWatermarkAdded) {
                    WatermarkPageEvent watermarkPageEvent = new WatermarkPageEvent();
                    watermarkPageEvent.setWatermark(mWatermark);
                    writer.setPageEvent(watermarkPageEvent);
                }

                document.open();

                float pageWidth = document.getPageSize().getWidth() - (mMarginLeft + mMarginRight);
                float pageHeight = document.getPageSize().getHeight() - (mMarginBottom + mMarginTop);
                if (mImagescaleType.equals(PdfLib.IMAGE_SCALE_TYPE_ASPECT_RATIO))
                    image.scaleToFit(pageWidth, pageHeight);
                else
                    image.scaleAbsolute(pageWidth, pageHeight);

                image.setAbsolutePosition(
                        (documentRect.getWidth() - image.getScaledWidth()) / 2,
                        (documentRect.getHeight() - image.getScaledHeight()) / 2);

                addPageNumber(documentRect, writer);
                document.add(image);
                document.newPage();

            }
        } catch (Exception ex) {
            mSuccess = false;
            ex.printStackTrace();
        } finally {
            if (document != null) {
                document.close();
            }
        }

        return null;
    }

    private void addPageNumber(Rectangle documentRect, PdfWriter writer) {
        if (mPageNumStyle != null) {
            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_BOTTOM,
                    getPhrase(writer, mPageNumStyle, mImagesUri.size()),
                    ((documentRect.getRight() + documentRect.getLeft()) / 2),
                    documentRect.getBottom() + 25, 0);
        }
    }

    @NonNull
    private Phrase getPhrase(PdfWriter writer, String pageNumStyle, int size) {
        Phrase phrase;
        switch (pageNumStyle) {
            case PdfLib.PG_NUM_STYLE_PAGE_X_OF_N:
                phrase = new Phrase(String.format("Page %d of %d", writer.getPageNumber(), size));
                break;
            case PdfLib.PG_NUM_STYLE_X_OF_N:
                phrase = new Phrase(String.format("%d of %d", writer.getPageNumber(), size));
                break;
            default:
                phrase = new Phrase(String.format("%d", writer.getPageNumber()));
                break;
        }
        return phrase;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        mOnPDFCreatedInterface.onPDFCreated(mSuccess, mPath);
    }

    /**
     * Read the BaseColor of passed color
     *
     * @param color value of color in int
     */
    private BaseColor getBaseColor(int color) {
        return new BaseColor(
                Color.red(color),
                Color.green(color),
                Color.blue(color)
        );
    }
}



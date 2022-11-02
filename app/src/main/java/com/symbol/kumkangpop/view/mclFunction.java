package com.symbol.kumkangpop.view;
public class mclFunction {

    public static String fnBarcodeConvertPrint(String sReceiveString)
    {
        /*if (sReceiveString.length() > 20) sReceiveString = sReceiveString.substring(0, 14);

        String[] saction = sReceiveString.split("-");
        if (saction.length > 2)
        {
            saction[0] += "-" + saction[1];

            mdbPOPControl npopControl = new mdbPOPControl();
            DataSet nDataSet = null;
            bool bcheck = false;

            if (npopControl.GetNumSeekData(2, saction[0]) && saction[2] == "03")
            {
                nDataSet = npopControl.GetNumConvertData(1, saction[0]);
                if (nDataSet.Tables[0].Rows.Count == 0)
                {
                    ////nScaner.fnBeepError();
                    mclMsgBox.Show(mclVariable.Language == 0 ? "출력 할 정보가 없습니다." : "Do not output information.");
                }
                else
                {
                    nDataSet = npopControl.GetPackingDataExists(nDataSet.Tables[0].Rows[0][0].ToString(), mclVariable.LocationNo, saction[0]);
                    if (nDataSet.Tables[0].Rows.Count == 0)
                    {
                        ////nScaner.fnBeepError();
                        mclMsgBox.Show(mclVariable.Language == 0 ? "출력 할 정보가 없습니다." : "Do not output information.");
                    }
                    else
                    {
                        fnSetPrintOrderData(2, saction[0]);
                    }
                }

                sReceiveString = "";
            }
            else if (npopControl.GetNumSeekData(4, saction[0]) && saction[2] == "03")
            {
                nDataSet = npopControl.GetStockOutDataExists(mclVariable.LocationNo, 3, saction[0]);
                if (nDataSet.Tables[0].Rows.Count == 0)
                {
                    ////nScaner.fnBeepError();
                    mclMsgBox.Show(mclVariable.Language == 0 ? "출력 할 정보가 없습니다." : "Do not output information.");
                }
                else
                {
                    fnSetPrintOrderData(5, saction[0]);
                }

                sReceiveString = "";
            }
            else if (npopControl.GetNumSeekData(5, saction[0]) && saction[2] == "04")
            {
                nDataSet = npopControl.GetStockOutDataStockOutType(0, saction[0]);
                if (nDataSet.Tables[0].Rows.Count != 0)
                {
                    if (nDataSet.Tables[0].Rows[0]["StockInFlag"].ToString() == "0")
                    {
                        if (mclMsgBox.Show(mclVariable.Language == 0 ? "선택한 출고정보를 완료하시겠습니까?(Yes:F3/No:F4)" : "Would you like to complete the selected factory information? (Yes: F3/No: F4)", "", MessageBoxButtons.OKCancel, MessageBoxIcon.Question, MessageBoxDefaultButton.Button1) == DialogResult.OK)
                        {
                            bcheck = npopControl.UpdateStockOutDataStockInFlag(saction[0], 1, mclVariable.UserId);
                            if (bcheck)
                            {
                                mclMsgBox.Show(mclVariable.Language == 0 ? "제품출고를 완료하였습니다." : "You have completed the product shipped.");
                            }
                            else
                            {
                                ////nScaner.fnBeepError();
                                mclMsgBox.Show(mclVariable.Language == 0 ? "진행중에 오류가 발생하였습니다." : "An error occurred during the course.");
                            }
                        }
                    }
                    else
                    {
                        if (mclMsgBox.Show(mclVariable.Language == 0 ? "선택한 출고정보를 완료취소하시겠습니까?(Yes:F3/No:F4)" : "Are you sure you want to cancel the selected factory, complete information? (Yes: F3/No: F4)", "", MessageBoxButtons.OKCancel, MessageBoxIcon.Question, MessageBoxDefaultButton.Button1) == DialogResult.OK)
                        {
                            bcheck = npopControl.UpdateStockOutDataStockInFlag(saction[0], 0, mclVariable.UserId);
                            if (bcheck)
                            {
                                mclMsgBox.Show(mclVariable.Language == 0 ? "제품출고를 취소하였습니다." : "Product has been shipped, cancel.");
                            }
                            else
                            {
                                ////nScaner.fnBeepError();
                                mclMsgBox.Show(mclVariable.Language == 0 ? "진행중에 오류가 발생하였습니다." : "An error occurred during the course.");
                            }
                        }
                    }

                    sReceiveString = "";
                }
            }
        }
*/
        return sReceiveString;
    }
}

package com.nit.service;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.nit.binding.COSummary;
import com.nit.entity.CitizenAppRegistrationEntity;
import com.nit.entity.CoTriggersEntity;
import com.nit.entity.DcCaseEntity;
import com.nit.entity.ElgibilityDetailsEntity;
import com.nit.repository.ICitizenRegistrationRepository;
import com.nit.repository.ICoTriggersRepository;
import com.nit.repository.IDcCaseRepository;
import com.nit.repository.IElgibilityDetermineRepository;
import com.nit.utils.EmailUtils;

@Service
public class CorrespondenceMgmtServiceImpl implements ICorrespondenceMgmtService {
    
	@Autowired
	private ICoTriggersRepository  triggerRepo;
    @Autowired
    private IElgibilityDetermineRepository   elgibileRepo;
    @Autowired
    private IDcCaseRepository  caseRepo;
    @Autowired
    private ICitizenRegistrationRepository  citizenRepo;
    @Autowired
    private EmailUtils  mailUtil;
	@Override
	public COSummary proccessPendingTriggers() {
		CitizenAppRegistrationEntity  citizenEntity = null;
		ElgibilityDetailsEntity  elgiEntity=null;
		  int pendingTriggers=0;
		   int successTrigger=0;
		// get all pending triggers
		List<CoTriggersEntity> triggerlist = triggerRepo.findByTriggerStatus("pending");
		//process each pending trigger 
		for(CoTriggersEntity triggerEntity:triggerlist) {
			//get elgibility details based on caseno
			  elgiEntity = elgibileRepo.findByCaseNo(triggerEntity.getCaseNo());
			 //get appId based on caseNo
			Optional<DcCaseEntity> optEntity =caseRepo.findById(triggerEntity.getCaseNo());
			if(optEntity.isPresent()) {
				DcCaseEntity  caseEntity = optEntity.get();
				Integer  appId = caseEntity.getAppId();
				//get citizen details based on appId
				Optional<CitizenAppRegistrationEntity>  optcitizenEntity = citizenRepo.findById(appId);
				if(optcitizenEntity.isPresent()) {
					citizenEntity = optcitizenEntity.get();
				}
			}
			//generate pdf doc having elgibility details
			 
			try{
				generatePdfAndSendMail(elgiEntity, citizenEntity);
				successTrigger++;
			}catch(Exception e) {
				pendingTriggers++;
				e.printStackTrace();
			}
			
		}//for loop
		COSummary  summary = new COSummary();
		summary.setTotalTriggers(triggerlist.size());
		summary.setPendingTriggers(pendingTriggers);
		summary.setSuccessTriggers(successTrigger);
		return summary;
	}
    private void generatePdfAndSendMail(ElgibilityDetailsEntity elgiEntity, CitizenAppRegistrationEntity citizenEntity)throws Exception {
    	
		//create Document obj(openPdf)
        Document document = new Document(PageSize.A4);
        File file = new File(elgiEntity.getCaseNo()+" .pdf");
        FileOutputStream  fos = new FileOutputStream(file);
        
        //get Pdfwriter to write  to the document and response obj
        PdfWriter.getInstance(document, fos);
        //open the document
        document.open();
        //define font for the paragraph 
        Font font = FontFactory.getFont(FontFactory.TIMES_BOLDITALIC);
        font.setSize(30);
        font.setColor(Color.CYAN);
        
        //create  the paragraph having content and above font style
        Paragraph para = new Paragraph("Search Report of Courses",font);
        para.setAlignment(Paragraph.ALIGN_CENTER);
        //add paragraph  to document
        document.add(para);
        
       //Display search results as the pdf table
        PdfPTable  table = new PdfPTable(10);
        table.setWidthPercentage(70);
        table.setWidths(new float[] {3.0f,3.0f,3.0f,3.0f,3.0f,3.0f,3.0f,3.0f,3.0f,3.0f});
        table.setSpacingBefore(2.0f);
        
        //prepare heading row cells in the pdf table
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.gray);
        cell.setPadding(5);
        Font cellFont = FontFactory.getFont(FontFactory.COURIER_BOLD);
        cellFont.setColor(Color.BLACK);
        
        cell.setPhrase(new Phrase("edTraceId", cellFont));
        table.addCell(cell);
        cell.setPhrase(new Phrase("caseNo", cellFont));
        table.addCell(cell);
        cell.setPhrase(new Phrase("holderName", cellFont));
        table.addCell(cell);
        cell.setPhrase(new Phrase("holderSSN", cellFont));
        table.addCell(cell);
        cell.setPhrase(new Phrase("planName", cellFont));
        table.addCell(cell);
        cell.setPhrase(new Phrase("planStatus", cellFont));
        table.addCell(cell);
        cell.setPhrase(new Phrase("planStartDate", cellFont));
        table.addCell(cell);
        cell.setPhrase(new Phrase("planEndDate", cellFont));
        table.addCell(cell);
        cell.setPhrase(new Phrase("benefitAmt", cellFont));
        table.addCell(cell);
        cell.setPhrase(new Phrase("denialReason", cellFont));
        table.addCell(cell);
        
        //add data cells to pdf table
        
        	table.addCell(String.valueOf(elgiEntity.getEdTraceId()));
        	table.addCell(String.valueOf(elgiEntity.getCaseNo()));
        	table.addCell(elgiEntity.getHolderName());
        	table.addCell(String.valueOf(elgiEntity.getHolderSSN()));
        	table.addCell(elgiEntity.getPlanName());
        	table.addCell(elgiEntity.getPlanStatus());
        	table.addCell(String.valueOf(elgiEntity.getPlanStartDate()));
        	table.addCell(String.valueOf(elgiEntity.getPlanEndDate()));
        	table.addCell(String.valueOf(elgiEntity.getBenefitAmt()));
        	table.addCell(elgiEntity.getDenialReason());
      
        //add table to document
        document.add(table);
        //close the document
        document.close();
        //send the generated pdf docs  as the email message
        String subject = " plan approval/deniel mail ";
        String body = "hello Mr/Miss/Mrs."+citizenEntity.getFullName()+", This mail contains complete details plan approval or denial";
        mailUtil.sendEmail(citizenEntity.getEmail(),subject, body,file);;
        //update  Co_triggers table
        updateCoTrigger(elgiEntity.getCaseNo(),file);
    }
         private void updateCoTrigger(Integer caseNo, File file)throws Exception {
        	 //check trigger availability based on caseNo
        	 CoTriggersEntity  triggerEntity = triggerRepo.findByCaseNo(caseNo);
        	 //get byte[] representing pdf doc content
        	 byte[]  pdfContent = new byte[(int)file.length()];
        	 FileInputStream fis = new FileInputStream(file);
        	 fis.read(pdfContent);
        	 if(triggerEntity!=null) {
        		 triggerEntity.setCoNoticePdf(pdfContent);
        		 triggerRepo.save(triggerEntity);
        	 }
        	 fis.close();
         }
}

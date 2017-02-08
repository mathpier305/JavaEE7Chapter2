/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathurin.javaee7.chapter2;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 *
 * @author math305
 */
@WebServlet(name="DisplayAlbumServlet",
        urlPatterns ={"/DisplayAlbumServlet"})
@MultipartConfig()
public class DisplayAlbumServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet DisplayAlbumServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet DisplayAlbumServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            handleRequest(request, response);
      
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handleRequest(request, response);
    }

    private void handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        ServletContext servletContext = request.getServletContext();
        PhotoAlbum pa = PhotoAlbum.getPhotoAlbum(servletContext);
        if(request.getContentType() != null && request.getContentType().startsWith("multipart/form-data")){
          this.uploadPhoto(request, pa);
          
        }
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        try{
            writer.write("<html>");
            writer.write("<head>");
            writer.write("<title>Photo view </title>");
            writer.write("</head>");
            writer.write("<body>");
            writer.write("<h3 align='center'>Photos</h3>");
            this.displayAlbum(pa, "", writer);
            writer.println("</body>");
            writer.println("</html>");
        }finally{
            writer.close();
            
        }
        
        
    }
    
    
    private void displayAlbum(PhotoAlbum pa, String label, PrintWriter writer){
        writer.write("<h3 align='center' >"+label+"</h3>");
        writer.write("<table align='center'>");
        for(int j=0; j <pa.getPhotoCount(); j++){
            writer.write("<td>");
            writer.write("<a href='./DisplayPhotoServlet?photo="+j+"'>");
            writer.write("<img src='./DisplayPhotoServlet?photo="+j+"' alt='photo' height='120' width='150' >");
            writer.write("</a>");
            writer.write("</td>");
        }
        writer.write("<td bgcolor='#cccccc' width='120' height='120'>");
        writer.write("<form align='left' action='DisplayAlbumServlet' method='post' enctype='multipart/form-data'");
        writer.write("<input value='choose' name ='myFile' type='file' accept='image/jpeg'> <br>");
        writer.write("<input value='Upload' type='submit\'><br >");
        writer.write("</form>");
        writer.write("</td>");
        writer.write("</tr>");
        writer.write("<tr>");
        for(int j=0; j < pa.getPhotoCount(); j++){
            writer.write("<td align='center'>");
            writer.write(pa.getPhotoName(j));
            writer.write("</td>");
        }
        writer.write("</tr>");
        writer.write("<tr>");
        
        for(int j=0; j < pa.getPhotoCount(); j++){
            writer.write("<td align='center'>");
            writer.write("<a href='RemovePhotoServlet?photo="+j+"'> remove </a>");
            writer.write("</td>");
        }
        writer.write("</tr>");
        writer.write("</table>");
    }
    private void uploadPhoto(HttpServletRequest request, PhotoAlbum pa) throws IOException, ServletException{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String filename =null;
        
        for(Part p: request.getParts()){
           this.copyBytes(p.getInputStream(), baos);
           filename= p.getSubmittedFileName();
        }
        
        if(!"".equals(filename)){
            String photoName =filename.substring(0, filename.lastIndexOf(","));
            pa.addPhoto(photoName, baos.toByteArray());
        }
    }
    
    private void copyBytes(InputStream is, OutputStream os) throws IOException{
        int i;
        while((i=is.read()) != -1){
            os.write(i);
        }
        is.close();
        os.close();
    }
    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}

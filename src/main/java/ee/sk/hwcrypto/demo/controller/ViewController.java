/**
 * JDigiDoc Hwcrypto Demo
 *
 * The MIT License (MIT)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package ee.sk.hwcrypto.demo.controller;

import ee.sk.digidoc.DataFile;
import ee.sk.digidoc.DigiDocException;
import ee.sk.digidoc.SignedDoc;
import ee.sk.hwcrypto.demo.model.SigningSessionData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class ViewController {

    private static final Logger log = LoggerFactory.getLogger(ViewController.class);
    public static final String CONTAINER_MIME_TYPE = "application/vnd.etsi.asic-e+zip";
    @Autowired
    private SigningSessionData session;

    @RequestMapping("")
    public String view() {
        return "view";
    }

    @RequestMapping("/downloadContainer")
    public void downloadContainer(HttpServletResponse response) {
        SignedDoc signedDoc = session.getSignedDoc();
        String fileName = ((DataFile) signedDoc.getDataFiles().get(0)).getFileName() + ".bdoc";
        response.setContentType(CONTAINER_MIME_TYPE);
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            signedDoc.writeToStream(outputStream);
            response.flushBuffer();
        } catch (IOException | DigiDocException e) {
            log.error("Error Writing file content to output stream", e);
        }
    }
}
package com.m01project.taskmanager.service;

import com.m01project.emailsender.application.port.EmailMessage;
import com.m01project.taskmanager.config.FrontendProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PasswordResetEmailComposer {

    private final FrontendProperties frontend;

    public EmailMessage compose(String to, UUID token) {
        String link = UriComponentsBuilder
                .fromHttpUrl(frontend.baseUrl())
                .path(frontend.resetPasswordPath())
                .queryParam("token", token.toString())
                .build()
                .toUriString();

        String subject = "Reset your password";

        String body = """
                <div style="background-color:#f6f8fb;padding:24px 0;font-family:Arial,Helvetica,sans-serif;color:#111827;">
                  <div style="max-width:560px;margin:0 auto;background:#ffffff;border:1px solid #e5e7eb;border-radius:12px;box-shadow:0 6px 20px rgba(0,0,0,0.06);overflow:hidden;">
                    <div style="padding:24px 28px;border-bottom:1px solid #e5e7eb;background:#f9fafb;">
                      <div style="font-size:18px;font-weight:700;letter-spacing:0.2px;">Task Manager</div>
                    </div>
                    <div style="padding:28px;">
                      <h2 style="margin:0 0 10px 0;font-size:20px;">Reset your password</h2>
                      <p style="margin:0 0 16px 0;font-size:14px;line-height:1.6;color:#374151;">
                        We received a request to reset your password. Click the button below to continue.
                      </p>
                      <div style="margin:24px 0;text-align:center;">
                        <a href="%s"
                           style="display:inline-block;background:#111827;color:#ffffff;text-decoration:none;padding:12px 20px;border-radius:8px;font-weight:600;">
                          Reset Password
                        </a>
                      </div>
                      <p style="margin:0 0 8px 0;font-size:13px;line-height:1.6;color:#6b7280;">
                        This link will expire in 15 minutes.
                      </p>
                      <p style="margin:0;font-size:13px;line-height:1.6;color:#6b7280;">
                        If you did not request this, you can safely ignore this email.
                      </p>
                    </div>
                    <div style="padding:16px 28px;border-top:1px solid #e5e7eb;background:#f9fafb;font-size:12px;color:#6b7280;">
                      <div>Need help? Reply to this email.</div>
                    </div>
                  </div>
                  <div style="max-width:560px;margin:12px auto 0 auto;text-align:center;font-size:11px;color:#9ca3af;">
                    © Task Manager
                  </div>
                </div>
                """.formatted(link);

        return new EmailMessage(to, subject, body, true);
    }
}

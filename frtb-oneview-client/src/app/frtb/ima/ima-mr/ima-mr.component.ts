import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { MessageDialogService } from 'src/app/common/message-dialog.service';
import { AuthService } from 'src/app/core/auth.service';

@Component({
  selector: 'app-ima-mr',
  templateUrl: './ima-mr.component.html',
  styleUrls: ['./ima-mr.component.scss']
})
export class ImaMrComponent {

  constructor(
    private http: HttpClient,
    private dialogService: MessageDialogService,
    private authService: AuthService,
  ) { }

}

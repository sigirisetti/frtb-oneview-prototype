import { TestBed } from '@angular/core/testing';

import { MessageDialogServiceService } from './message-dialog-service.service';

describe('MessageDialogServiceService', () => {
  let service: MessageDialogServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MessageDialogServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

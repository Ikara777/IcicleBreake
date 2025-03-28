# IcicleBreake

## JavaGUI를 활용한 스코어 게임

**" 귀여운 팽귄 캐릭터를 조작하는 게임 "**
- 포토샵, 일러스트 제작 능력을 활용하여 View 요소를 해결해 귀여운 펭귄 캐릭터가 고드름을 피하는 게임을 제작함.
- 떨어지는 오브젝트의 요소를 무작위로 설정하여 게임의 긴장감과 몰입감을 높임.
- ID를 기준으로 최고 점수를 기억하여 경쟁 심리를 끌어올려 흥미와 경쟁 심리를 자극함.

## 👨‍💻 프로젝트 진행자

<table>
  <tr>
    <th>최동훈</th>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/66037df4-f7e3-433e-92a5-17b40556ae2f" width="100"></td>
  </tr>
</table>


## 🛠 주요 기능

**🔐 로그인 및 회원가입**
 - 회원가입을 통하여 파일 데이터에 사용자 인증 정보를 저장 후 정보를 대조하여 로그인
 
**⌚ 캐릭터 움직임 조정**
 - 캐릭터가 일정한 속도로 좌우로 움직일 수 있으며 일정 범위를 넘어가지 않도록 범위 지정
 - 피사체의 속도와 크기를 무작위로 설정하므로 게임의 긴장감을 더하여 플레이어가 조작을 다채롭게 하도록 조정
 
**💪 Game Over 이후 Retry**
 - 하늘에서 떨어지는 피사체와 캐릭터가 충돌할 경우 Game Over 되며 점수 합산이 종료되고 게임이 정상적으로 다시 실행될 수 있도록 구현함.
   
**📅 High-score 점수 갱신**
 - 파일을 통한 데이터 저장 시스템을 구축하여 1초마다 점수가 갱신되며 이전 점수와 비교하여 플레이어의 최고 점수를 갱신함.

## 클래스 다이어그램
<img src="https://github.com/user-attachments/assets/72f49385-710c-4201-8fb8-429b81fcacb1" width="750">

## 🖥️ 화면 구성

<table>
  <tr>
    <th>로그인 화면</th>
    <th>회원가입 화면</th>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/9ee38e58-8a4c-4be2-872f-07056bd71087" width="400"></td>
    <td><img src="https://github.com/user-attachments/assets/b43fa175-e11f-4a91-b70e-2bcc7ce2c0f9" width="400"></td>
  </tr>
</table>

<table>
  <tr>
    <th>게임 화면</th>
    <th>게임 오버후 재시작</th>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/ab7bfe60-6657-4f3a-8b9c-5abdad37a67e" width="400"></td>
    <td><img src="https://github.com/user-attachments/assets/8dca45f1-590c-4ad7-a097-a01be3b9be48" width="400"></td>
  </tr>
</table>

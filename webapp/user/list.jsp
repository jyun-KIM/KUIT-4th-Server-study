<%@ include file="../include/navigation.jspf" %>
<%@ include file="../include/header.jspf" %>

<!doctype html>
<html lang="ko">
  <body>
    <div class="navbar-default">
        <header class="d-flex flex-wrap align-items-center justify-content-center justify-content-md-between py-3 mb-4 border-bottom">
            <a href="/" class="d-flex align-items-center col-md-3 mb-2 mb-md-0 text-dark text-decoration-none">
                <svg class="bi me-2" width="40" height="32" role="img" aria-label="Bootstrap"><use xlink:href="#bootstrap"/></svg>
            </a>

            <ul class="nav col-12 col-md-auto mb-2 justify-content-center mb-md-0">
                <li><a href="/index.html" class="nav-link px-2 link-secondary">Q&A</a></li>
                <li><a href="#" class="nav-link px-2 link-dark">FAQs</a></li>
                <li><a href="/user/userList" class="nav-link px-2 link-dark">User List</a></li>
                <li><a href="#" class="nav-link px-2 link-dark">About</a></li>
            </ul>

            <div class="col-md-3 text-end">
                <a href="/user/login.html" type="button" class="btn btn-outline-primary me-2">Login</a>
                <a href="/user/form.html" type="button" class="btn btn-primary">Sign-up</a>
            </div>
        </header>

    <div class="container" id="main">
        <table class="table table-striped">
            <thead class="col-md-12">
                <tr>
                <th class="col-md-3">#</th>
                <th class="col-md-3">아이디</th>
                <th class="col-md-3">이름</th>
                <th class="col-md-3">이메일</th>
                </tr>
            </thead>
            <tbody>
            <tr>
                <th class="col-md-3">1</th>
                <th class="col-md-3">lyouxsun</th>
                <th class="col-md-3">이영선</th>
                <th class="col-md-3">lyouxsun@kuit.kr</th>
            </tr>
            <tr>
                <th class="col-md-3">2</th>
                <th class="col-md-3">crohasang</th>
                <th class="col-md-3">조하상</th>
                <th class="col-md-3">crohasang@kuit.kr</th>
            </tr>
            <tr>
                <th class="col-md-3">3</th>
                <th class="col-md-3">yunjeongiya</th>
                <th class="col-md-3">이윤정</th>
                <th class="col-md-3">yunjeongiya@kuit.kr</th>
            </tr>
            <tr>
                <th class="col-md-3">4</th>
                <th class="col-md-3">hamhyeongju</th>
                <th class="col-md-3">함형주</th>
                <th class="col-md-3">hamhyeongju@kuit.kr</th>
            </tr>
            </tbody>
        </table>
    </div>
    
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
        <script src="../js/scripts.js"></script>
    </body>
</html>